import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ConfirmModalComponent } from '../../shared/components/confirm-modal/confirm-modal.component';
import { MatTableDataSource } from '@angular/material/table';
import { LogsService } from 'src/app/services/logs-services/logs.service';
import { SpinnerService } from 'src/app/services/shared-services/spinner.service';
import { LibranzaService } from 'src/app/services/libranza-service/libranza.service';
import { IpUtilService } from 'src/app/services/ip/ip-util.service';
import { AuthService } from '../../../../../services/auth-service/auth.service';
import { MatPaginator } from '@angular/material/paginator';

export const transaction = 'bp-librz-mto-segurosVoluntarios';

@Component({
  selector: 'app-seguros-voluntarios',
  templateUrl: './seguros-voluntarios.component.html',
  styleUrls: ['./seguros-voluntarios.component.scss'],
})
export class SegurosVoluntariosComponent implements OnInit {
  mostrarBotonEliminar: boolean = false;
  originalData = [];
    @ViewChild(MatPaginator, { static: true }) paginator!: MatPaginator;

  displayedColumns1: string[] = [
    'id',
    'nombrePlan',
    'valorPrima',
    'fallecimientoAccidental',
    'inhabilitacionTotal',
    'enfermedadesGraves',
    'canastaFallecimiento',
    'estadoPlan',
    'asalariado',
    'independiente',
    'pensionado',
    'edadMinima',
    'edadMinimaDias',
    'edadMaxima',
    'edadMaximaDias',
    'accion',
  ];

  dataSource = new MatTableDataSource<any>();

  columnasTabla = [
    { valor: 'nombrePlan', nombre: 'Nombre del plan' },
    { valor: 'valorPrima', nombre: 'Valor de la Prima (mensual)' },
    { valor: 'fallecimientoAccidental', nombre: 'Fallecimiento Accidental' },
    { valor: 'inhabilitacionTotal', nombre: 'Inhabilitación Total y Permanente y Desmenbración' },
    { valor: 'enfermedadesGraves', nombre: 'Enfermedades Graves' },
    { valor: 'canastaFallecimiento', nombre: 'Canasta por fallecimiento' },
    { valor: 'estadoPlan', nombre: 'Estado del Plan' },
    { valor: 'asalariado', nombre: 'Ocupación Asalariado' },
    { valor: 'independiente', nombre: 'Ocupación Independiente' },
    { valor: 'pensionado', nombre: 'Ocupación Pensionado' },
    { valor: 'edadMinima', nombre: 'Edad mínima (en años)' },
    { valor: 'edadMinimaDias', nombre: 'Edad mínima (en días)' },
    { valor: 'edadMaxima', nombre: 'Edad máxima (en años)' }, // Asumido como el valor correspondiente
    { valor: 'edadMaximaDias', nombre: 'Edad máxima (en días)' },
  ];
  creandoFila: boolean = false;
  isCreating: boolean;

  statusOptions = [
    {
      value: true,
      label: 'Vigente',
    },
    { value: false, label: 'No vigente' },
  ];
  statusconsult: [] = [];

  generalOptions = [
    { value: true, label: 'Si' },
    { value: false, label: 'No' },
  ];

  checked: boolean = true;
  valoresAnteriores: { [key: string]: any } = {};

  historico: {
    valorModificado: any;
    id: number;
    usuario: string;
    fecha_hora: string;
    ip: string;
    transaccion: number;
  }[];
  cancelarEdicionview: boolean;

  constructor(
    private dialog: MatDialog,
    private libranzaService: LibranzaService,
    private spinnerService: SpinnerService,
    private logsServices: LogsService,
    private _ipS: IpUtilService,
    private authService: AuthService
  ) {
    this.dataSource = new MatTableDataSource();
  }

  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
  }

  ngOnInit(): void {
    this.obtenerLogs();
    this.getSegurosVoluntariosFind();
    this.dataSource.paginator = this.paginator;
  }

  // Método para habilitar la edición de una fila
  habilitarEdicion(fila): void {
    fila.editando = true;
    this.cancelarEdicionview = true;
    fila.valor_prima = this.changeValue(fila.valor_prima.replace('$', ''));
    fila.fallecimiento_accidental = this.changeValue(
      fila.fallecimiento_accidental.replace('$', '')
    );
    fila.inhabilitacion_total = this.changeValue(fila.inhabilitacion_total.replace('$', ''));
    fila.enfermedades_graves = this.changeValue(fila.enfermedades_graves.replace('$', ''));
    fila.canasta_fallecimiento = this.changeValue(fila.canasta_fallecimiento.replace('$', ''));
    this.statusOptions;
    this.generalOptions;
  }

  validarNumerico(event): void {
    /**validación que sea 15 digitos */
    const valorPrima = event.target.value;
    if (valorPrima.length >= 15) {
      event.preventDefault();
    }

    /**validacion que sea numerico */
    const charCode = event.which ? event.which : event.keyCode;
    const inputChar = String.fromCharCode(charCode);
    if (!/^\d*\.?\d*$/.test(inputChar) && charCode !== 8) {
      event.preventDefault();
    }
  }

  /**validacion de campos edad */
  validacionCamposEdad(event): void {
    /**validación que sea 3 digitos */
    const valorPrima = event.target.value;
    if (valorPrima.length >= 3) {
      event.preventDefault();
    }

    /**validacion que sea numerico */
    const charCode = event.which ? event.which : event.keyCode;
    const inputChar = String.fromCharCode(charCode);
    if (!/^\d*\.?\d*$/.test(inputChar) && charCode !== 8) {
      event.preventDefault();
    }
  }

  // Método para guardar los cambios después de editar una fila
  guardarCambios(fila): void {
    let esValido = true;
    const camposExcluir = ['id', 'usuario', 'fecha_hora', 'ip', 'editando', 'accion'];
    const camposValidar = [
      'valor_prima',
      'fallecimiento_accidental',
      'inhabilitacion_total',
      'enfermedades_graves',
      'canasta_fallecimiento',
    ];

    const camposValidarAnios = [
      'edad_minima',
      'edad_minima_dias',
      'edad_maxima',
      'edad_maxima_dias',
    ];
    const errores = {};

    // Validar campos en null o vacíos
    Object.keys(fila).forEach(campo => {
      if (!camposExcluir.includes(campo)) {
        const valor = fila[campo];
        if (valor === null || valor === '') {
          errores[campo] = true;
          esValido = false;
          return;
        }
      }
    });

    // Validar campos en cero si el nombre del plan no es 'SIN SEGURO VOLUNTARIO'
    if (fila.nombre_plan.toLowerCase() !== 'sin seguro voluntario') {
      camposValidar.forEach(campoX => {
        if (fila[campoX] === '0' || fila[campoX] === 0) {
          errores[campoX] = true;
          esValido = false;
          return;
        }
      });
    }

    camposValidarAnios.forEach(campoAnio => {
      const valorAnio = fila[campoAnio];

      if (parseInt(fila.edad_minima) <= 17 || parseInt(fila.edad_minima) >= 998) {
        errores['edad_minima'] = true;
        esValido = false;
      }

      if (fila.edad_maxima == 0) {
        errores['edad_maxima'] = true;
        esValido = false;
      }

      if (fila.edad_minima_dias >= 365) {
        errores['edad_minima_dias'] = true;
        esValido = false;
      }

      if (fila.edad_maxima_dias >= 365) {
        errores['edad_maxima_dias'] = true;
        esValido = false;
      }
    });

    fila.errores = errores;

    if (esValido) {
      const dialogRef = this.dialog.open(ConfirmModalComponent, {
        data: { title: '¿Estás seguro/a de actualizar la información?', isConfirm: true },
      });

      dialogRef.afterClosed().subscribe((res: boolean) => {
        if (res) {
          fila.editando = false;

          if (fila.id != '') {
            delete fila.editando;
            (fila.usuario = this.authService.getUser() ?? 'user.update'), //localStorage.getItem('user')
              (fila.fecha_hora = this.obtenerFechas());
            fila.ip = this._ipS.actualUserIP;
            delete fila.errores;
            delete fila.accion;
            this.updateSegurosVoluntarios(fila);
          } else {
            delete fila.id;
            delete fila.editando;
            fila.id = this.encontrarMayorIdPlan() + 1;
            (fila.usuario = this.authService.getUser() ?? 'user.save'), // this.authService.getUser()
              (fila.fecha_hora = this.obtenerFechas());
            fila.ip = this._ipS.actualUserIP;
            delete fila.errores;

            this.saveSegurosVoluntarios(fila);
          }
        }
      });
    }
  }

  eliminarFila(fila): void {
    const filaAEliminar = {
      id: fila.id,
      estado: false,
      estado_plan: false,
      usuario: this.authService.getUser() ?? 'user.Delete',
      fecha_hora: this.obtenerFechas(),
      ip: this._ipS.actualUserIP,
    };
    const dialogRef = this.dialog.open(ConfirmModalComponent, {
      data: { title: '¿Estás seguro/a de eliminar esta fila de la tabla?', isConfirm: true },
    });
    dialogRef.afterClosed().subscribe((res: boolean) => {
      if (res) {
        this.deleteSegurosVoluntarios(filaAEliminar);
      } else {
      }
    });
  }

  agregarFila() {
    this.paginator.lastPage();
    const nuevaFila = {
      id: '',
      nombre_plan: '',
      valor_prima: '',
      fallecimiento_accidental: '',
      inhabilitacion_total: '',
      enfermedades_graves: '',
      canasta_fallecimiento: '',
      estado_plan: true,
      asalariado: true,
      independiente: true,
      pensionado: true,
      edad_minima: 0,
      edad_minima_dias: 0,
      edad_maxima: 0,
      edad_maxima_dias: 0,
      editando: true, // Establece editando en true para que los campos sean editables
    };

    this.dataSource.data.push(nuevaFila);
    this.dataSource.data = [...this.dataSource.data];
    this.creandoFila = true; // Cambiar el estado a true
    this.paginator.lastPage();
  }

  encontrarMayorIdPlan(): number {
    let maxIdPlan = Number.MIN_SAFE_INTEGER;
    this.dataSource.data.forEach(obj => {
      if (obj.id > maxIdPlan) {
        maxIdPlan = obj.id;
      }
    });
    return maxIdPlan;
  }

  cancelarCreacion(): void {
    const dialogRef = this.dialog.open(ConfirmModalComponent, {
      data: { title: '¿Estás seguro/a de salir sin guardar cambios?', isConfirm: true },
    });
    dialogRef.afterClosed().subscribe((res: boolean) => {
      if (res) {
        this.dataSource.data.pop(); // Eliminar la última fila agregada
        this.dataSource.data = [...this.dataSource.data]; // Notificar a Angular sobre el cambio
        this.creandoFila = false; // Cambiar el estado a false
      } else {
      }
    });
  }

  saveSegment(fila) {
    const dialog = this.dialog.open(ConfirmModalComponent, {
      data: { title: fila.data, isConfirm: false, txt_btn_ok: 'Entendido' },
    });
    dialog.afterClosed().subscribe(res => {
      if (res) {
        this.historico = [];
        setTimeout(() => {
          this.obtenerLogs();
        }, 1000);
      }
    });
  }

  getBloquearTabla(event) {
    const dialog = this.dialog.open(ConfirmModalComponent, {
      data: {
        title: event
          ? '¿Estas seguro/a de activar esta tabla de datos?'
          : '¿Estas seguro/a de inactivar esta tabla de datos?',
        isConfirm: true,
      },
    });
    dialog.afterClosed().subscribe(res => {
      if (res) {
        this.dialog.open(ConfirmModalComponent, {
          data: {
            title: event
              ? 'Se ha activado la información de la tabla'
              : 'Se ha inactivado la información de la tabla',
            isConfirm: false,
            txt_btn_ok: 'Entendido',
          },
        });
      } else {
        this.checked = !this.checked;
      }
    });
  }

  /**modificacion o cambio de formatos */

  formatCurrency(value: string): string {
    let datoNum = parseInt(value); // Convertir la cadena a número entero
    return datoNum.toLocaleString('es-CO', {
      style: 'currency',
      currency: 'COP',
      minimumFractionDigits: 0,
    });
  }

  changeValue(valorPrima: string): number {
    const valorSinSeparadores = valorPrima.replace(/\./g, '').replace(',', '.');
    return parseFloat(valorSinSeparadores);
  }

  formatearSeparadorMiles(event): void {
    let valorPrima = event.target.value;
    valorPrima = valorPrima.replace(/,/g, '');
    if (!isNaN(parseFloat(valorPrima))) {
      const numeroFormateado = parseFloat(valorPrima).toLocaleString('es-CO', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2,
      });
      event.target.value = numeroFormateado;
    }
  }

  /**********llamado a los Services */

  getSegurosVoluntariosFind() {
    this.spinnerService.show();
    this.libranzaService.getVoluntaryInsurancefind().subscribe(res => {
      this.cancelarEdicionview = false;
      this.spinnerService.hide();
      this.dataSource.data = res.data.map(item => {
        return {
          ...item,
          valor_prima: this.formatCurrency(item.valor_prima),
          fallecimiento_accidental: this.formatCurrency(item.fallecimiento_accidental),
          inhabilitacion_total: this.formatCurrency(item.inhabilitacion_total),
          enfermedades_graves: this.formatCurrency(item.enfermedades_graves),
          canasta_fallecimiento: this.formatCurrency(item.canasta_fallecimiento),
        };
      });
    });

  }

  saveSegurosVoluntarios(body) {
    this.spinnerService.show();

    body.id = Number(body.id);
    this.libranzaService.getVoluntaryInsuranceSave(body).subscribe(res => {
      this.creandoFila = false;
      this.getSegurosVoluntariosFind();
      this.saveSegment(res);

      this.spinnerService.hide();
    });
  }

  updateSegurosVoluntarios(body) {
    this.spinnerService.show();

    body.id = Number(body.id);
    this.libranzaService.getVoluntaryInsuranceUpdate(body).subscribe(res => {
      this.getSegurosVoluntariosFind();
      this.saveSegment(res);
      this.spinnerService.hide();
    });
  }

  deleteSegurosVoluntarios(body) {
    this.spinnerService.show();
    body.id = Number(body.id);
    this.libranzaService.getVoluntaryInsuranceUpdateStatus(body).subscribe(res => {
      this.getSegurosVoluntariosFind();
      this.saveSegment(res);
      this.spinnerService.hide();
    });
  }

  /**********************logs**************************************************/

  filterStatus(id) {
    // const data = this.statusOptions.filter(res => res.id ===Number(id));
    // return data[0]?.label
    id = String(id);
    const test = {
      true: 'Vigente',
      false: 'No vigente',
    };

    return test[id];
  }

  filterValid(id) {
    // const data = this.statusOptions.filter(res => res.id ===Number(id));
    // return data[0]?.label

    const test = {
      true: 'SI',
      false: 'NO',
    };

    return test[id];
  }

  obtenerLogs() {
    this.spinnerService.show();
    this.logsServices.getLogsTransaction(transaction).subscribe(res => {
      const result: any = res.map(item => {
        return {
          ...item,
          valorModificado: JSON.parse(item.valorModificado),
          valorAnterior:
            item.valorAnterior != '' ? JSON.parse(item.valorAnterior.replace("''", '')) : '',
        };
      });

      for (let index = 0; index < result.length; index++) {
        const nuevoObjeto: any = [];
        for (const [key, value] of Object.entries(result[index].valorModificado)) {
          if (key != 'id') {
            let valorAnterior: any = '';
            let valorNuevo: any = value;
            if (result[index].valorAnterior != '') {
              for (const [keyA, valueA] of Object.entries(result[index].valorAnterior)) {
                if (keyA == key) {
                  valorAnterior = valueA;
                }
              }
            }

            let columnaNombre = this.filterNames(key);

            // Asignar los valores divididos al nuevo objeto
            switch (key) {
              case 'valorPrima':
              case 'fallecimientoAccidental':
              case 'inhabilitacionTotal':
              case 'enfermedadesGraves':
              case 'canastaFallecimiento':
                {
                  valorNuevo = this.formatValue(valorNuevo.replace(':', ' '));
                  valorAnterior =
                    valorAnterior === '' ? '' : this.formatValue(valorAnterior.replace(':', ' '));
                }
                break;
              case 'estadoPlan':
                valorNuevo = valorNuevo.replace(':', '') === 'true' ? 'Vigente' : 'No vigente';
                valorAnterior =
                  valorAnterior.replace(':', '') === ''
                    ? ''
                    : valorAnterior === 'true'
                    ? 'Vigente'
                    : 'No vigente';
                break;
              case 'asalariado':
              case 'independiente':
              case 'pensionado':
                valorNuevo = valorNuevo === 'true' ? 'Si' : 'No';
                valorAnterior = valorAnterior === '' ? '' : valorAnterior === 'true' ? 'Si' : 'No';
                break;
              default:
                valorNuevo = valorNuevo.replace(':', ' ');
                valorAnterior = valorAnterior === '' ? '' : valorAnterior.replace(':', ' ');
                break;
            }
            nuevoObjeto.push({
              campo: columnaNombre,
              nuevoValor: valorNuevo,
              valorAnterior: valorAnterior,
            });
          }
          result[index].valorModificado = {
            id: result[index].valorModificado.id,
            cambios: nuevoObjeto,
          };

          // result[index].valorModificado = nuevoObjeto
        }
      }

      this.spinnerService.hide();

      console.log(result);
      this.historico = result;
    });
  }

  obtenerFechas(): String {
    const now = new Date();

    const day = String(now.getDate()).padStart(2, '0');
    const month = String(now.getMonth() + 1).padStart(2, '0'); // Los meses son base 0, así que se suma 1
    const year = now.getFullYear();

    const hours = String(now.getHours()).padStart(2, '0');
    const minutes = String(now.getMinutes()).padStart(2, '0');
    const seconds = String(now.getSeconds()).padStart(2, '0');
    const fecha_hora = `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;

    return fecha_hora;
  }

  filterNames(nombre) {
    const nombres = this.columnasTabla.filter(value => value.valor === nombre);
    return nombres[0].nombre;
  }

  formatValue(value) {
    const numericValue = parseFloat(value);
    const formattedValue = '$' + numericValue.toFixed(2).replace(/\d(?=(\d{3})+\.)/g, '$&,');
    return formattedValue;
  }

  exportDataToExcel() {
    this.libranzaService.exportToExcel(this.historico, 'reporte_seguros_voluntarios');
  }

  cancelarEdicion() {
    const dialog = this.dialog.open(ConfirmModalComponent, {
      data: { title: '¿Estas seguro de salir sin guardar cambios?', isConfirm: true },
    });
    dialog.afterClosed().subscribe(res => {
      if (res) {
        this.cancelarEdicionview = false;
        this.creandoFila = false; // Cambiar el estado a false
        this.getSegurosVoluntariosFind();
      } else {
      }
    });
  }
}
