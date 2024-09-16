import { Component, OnInit } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { SpinnerService } from 'src/app/services/shared-services/spinner.service';
import { MatDialog } from '@angular/material/dialog';
import { LibranzaService } from 'src/app/services/libranza-service/libranza.service';
import { RangoPlazoTasas } from './models/plazos';
import { ConfirmModalComponent } from '../../shared/components/confirm-modal/confirm-modal.component';
import { LogsService } from 'src/app/services/logs-services/logs.service';
import { IpUtilService } from 'src/app/services/ip/ip-util.service';
import { AuthService } from 'src/app/services/auth-service/auth.service';
import { PageEvent } from '@angular/material/paginator';
import { ITableContent, ITableHeaders } from '../../shared/interfaces/logs-table.interface';
import { UMapLogToTable, UOrderLogs } from '../../shared/utils/map-log-table.util';
import { createLogsResponse } from 'src/app/services/models/logs';
import moment from 'moment';

const LOGS_HEADERS_RANGE: ITableHeaders[] = [
  {
    name: 'id',
    key: 'recordid',
    type: 'text',
  },
  {
    name: 'Editor',
    key: 'usuario',
    type: 'text',
  },
  {
    name: 'Fecha y Hora de registro',
    key: 'fecha_hora',
    type: 'datetime',
  },
  {
    name: 'Plazo (entendido en meses)',
    key: 'descriptionPlazo',
    type: 'text',
  },
  {
    name: 'Campo modificado',
    key: 'editField',
    type: 'text',
  },
  {
    name: 'Valor Anterior',
    key: 'oldValue',
    type: 'text',
  },
  {
    name: 'Valor nuevo',
    key: 'newValue',
    type: 'text',
  },
];

@Component({
  selector: 'app-rango-plazo-tasas',
  templateUrl: './rango-plazo-tasas.component.html',
  styleUrls: ['./rango-plazo-tasas.component.scss'],
})
export class RangoPlazoTasasComponent implements OnInit {
  mostrarBotonEliminar: boolean = false;
  dataSource: MatTableDataSource<any>;
  creandoFila: boolean = false;
  records: any[] = [];
  transaction = "bp-librz-mto-rango-tasas";
  displayedColumns1: string[] = ['descriptionPlazo', 'descriptionStatus', 'accion'];
  reportName = "RANGOS DE PLAZO";
  statusOptions = [
    { value: 1, label: 'Activo' },
    { value: 2, label: 'Inactivo' },
  ];
  activeUsername:string;
  historico: any;
  cancelarEdicionview = false;
  errores: {};
  logsTable: ITableHeaders[] = LOGS_HEADERS_RANGE;
  columnasTabla = [
    { valor: 'descriptionPlazo', nombre: 'Plazo (entendido en Meses)' },
    { valor: 'statusId', nombre: 'Estado' },
  ];
  paginator: PageEvent = { length: 0, pageSize: 5, pageIndex: 0, previousPageIndex: 0 };
  paginateLogs: ITableContent = { content: [], headers: [] };
  logs: ITableContent;

  constructor(
    private dialog: MatDialog,
    private libranzaService: LibranzaService,
    private spinnerService: SpinnerService,
    private logsServices: LogsService,
    private _ipS: IpUtilService,private authService: AuthService
  ) {
    this.dataSource = new MatTableDataSource<any>();
    this.activeUsername = this.authService.getUser() ?? 'N/A';
  }

  ngOnInit(): void {
    this.getRangosPlazoTasasFind();
  }

  // Método para habilitar la edición de una fila}
  habilitarEdicion(fila): void {
    this.cancelarEdicionview = true;
    fila.editando = true;
  }

  guardarCambios(fila): void {
    /*inicio validaciones*/
    this.errores = {};
    let esValido = true;
    const regex: RegExp = new RegExp(/^\d+[-,><]\d+$|^[><]\d+$|^\d+[><]$/);

    const camposValidacion = ['description_plazo'];

    const camposExcluir = [
      'creado_por',
      'fecha_creacion',
      'actualizado_por',
      'fecha_actualizacion',
      'ip',
      'id_plazo',
      'editando',
    ];

    Object.keys(fila).forEach(campo => {
      if (!camposExcluir.includes(campo)) {
        const valor = fila[campo];
        if (valor === null || valor === '') {
          this.errores[campo] = true;
          esValido = false;
          return;
        }
      }
    });

    if (!regex.test(fila.description_plazo)) {
      this.errores['description_plazo'] = true;
      console.log(this.errores);
      esValido = false;
    }

    if (esValido) {
      const dialogRef = this.dialog.open(ConfirmModalComponent, {
        data: { title: '¿Estás seguro/a de actualizar la información?', isConfirm: true },
      });

      dialogRef.afterClosed().subscribe((res: boolean) => {
        if (res) {
          fila.editando = false;
          fila.ip = this._ipS.actualUserIP;
          this.creandoFila = false; // Cambiar el estado a false
          if (fila.id_plazo != null) {
            delete fila.editando;
            fila.actualizado_por = this.activeUsername;
            fila.fecha_actualizacion = this.obtenerFechas();
            this.getRangosPlazoTasasUpdate(fila);
          } else {
            delete fila.editando;
            fila.ip = this._ipS.actualUserIP;
            fila.id_plazo = this.encontrarMayorIdPlan() + 1;
            fila.creado_por = this.activeUsername;
            fila.fecha_creacion = this.obtenerFechas();
            this.getRangosPlazoTasasSave(fila);
          }
        }
      });
    }
  }

  mostrarEliminar(){
    this.mostrarBotonEliminar=true
  }

  eliminarFila(fila): void {
    this.mostrarBotonEliminar=true
    const dialogRef = this.dialog.open(ConfirmModalComponent, {
      data: { title: '¿Estás seguro/a de eliminar esta fila de la tabla?', isConfirm: true },
    });
    dialogRef.afterClosed().subscribe((res: boolean) => {
      if (res) {
        this.getRangosPlazoTasasDelete(fila);
      } else {
      }
    });
  }

  agregarFila() {
    const nuevaFila: RangoPlazoTasas = {
      id_plazo: null,
      description_plazo: '',
      status_id: null,
      creado_por: null,
      fecha_creacion: null,
      actualizado_por: null,
      fecha_actualizacion: null,
      ip: '192.168.1.53',
      editando: true,
    };

    this.dataSource.data.push(nuevaFila);
    this.dataSource.data = [...this.dataSource.data];
    this.creandoFila = true; // Cambiar el estado a true
  }

  encontrarMayorIdPlan(): number {
    let maxIdPlazo = Number.MIN_SAFE_INTEGER;
    this.dataSource.data.forEach(obj => {
      if (obj.id_plazo > maxIdPlazo) {
        maxIdPlazo = obj.id_plazo;
      }
    });
    if(maxIdPlazo === Number.MIN_SAFE_INTEGER){
      maxIdPlazo = 0;
    }
    return maxIdPlazo;
  }

  cancelarEliminacion(): void {
    const dialogRef = this.dialog.open(ConfirmModalComponent, {
      data: { title: '¿Estás seguro/a de salir sin guardar cambios?', isConfirm: true },
    });
    dialogRef.afterClosed().subscribe((res: boolean) => {
      if (res) {
        this.mostrarBotonEliminar = false;
      } else {
        this.mostrarBotonEliminar = true;
      }
    });
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

  /**consumos al server-app */
  getRangosPlazoTasasFind() {
    this.spinnerService.show();
    this.libranzaService.getTermRateRangefind().subscribe(res => {
      this.records = res.data;
      this.dataSource.data = res.data;
      this.obtenerLogs();
      this.spinnerService.hide();
    });
  }

  getRangosPlazoTasasSave(body) {
    this.spinnerService.show();
    this.libranzaService.getTermRateRangeSave(body).subscribe(res => {
      this.getRangosPlazoTasasFind();
      this.saveSegment(res);
      this.spinnerService.hide();
    });
  }

  getRangosPlazoTasasUpdate(body) {
    this.spinnerService.show();
    this.libranzaService.getTermRateRangeUpdate(body).subscribe(res => {
      this.getRangosPlazoTasasFind();
      this.saveSegment(res);
      this.spinnerService.hide();
    });
  }

  getRangosPlazoTasasDelete(body) {
    this.libranzaService.getTermRateRangeDelete(body).subscribe(res => {
      this.getRangosPlazoTasasFind();
      this.saveSegment(res);
    });
  }

  /**********************logs**************************************************/

  filterStatus(id: string) {
    const data = this.statusOptions.filter(res => res.value === Number(id));
    return data[0]?.label;
  }

  obtenerLogs() {
    this.spinnerService.show();
    this.logsServices.getLogsTransaction(this.transaction).subscribe(res => {
      const logsOrder = UOrderLogs(res);
      const result: any = logsOrder.map(item => {
        return {
          ...item,
          valorModificado: JSON.parse(item.valorModificado),
          valorAnterior: item.valorAnterior != "" ? JSON.parse(item.valorAnterior.replace("'\'", '')):""
        };
      });
      const nuevoObjeto: any = [];
      for (let index = 0; index < result.length; index++) {
        let idRecord = result[index].valorModificado.idPlazo;

        let rc = this.records.find(res => res.id_plazo == idRecord);
        let description_plazo = "";
        if(rc != undefined){
          description_plazo = rc.description_plazo;
        }
        for (const [key, value] of Object.entries(result[index].valorModificado)) {
          if(key!="idPlazo")
          {
           let RecordDate = moment(result[index].fecha_hora).toDate()          

            let newValue:any = {};
            newValue.recordid = idRecord;
            newValue.fecha_hora = moment(RecordDate).format('YYYY-MM-DD HH:mm:ss');
            newValue.accion  = result[index].accion;
            newValue.usuario = result[index].usuario;
            newValue.id = result[index].id;
            newValue.ip = result[index].ip;
            newValue.transaccion = result[index].transaccion;
            newValue.valorModificado = JSON.stringify(result[index].valorModificado);
            newValue.valorAnterior = JSON.stringify(result[index].valorAnterior);
            let valorAnterior:any = "";
            let valorNuevo:any = value;
            if(result[index].valorAnterior != ""){
            for (const [keyA, valueA] of Object.entries(result[index].valorAnterior)) {
                  if(keyA == key){
                    valorAnterior = valueA
                  }
            }}
            let columnaNombre = this.filterNames(key);
            newValue.descriptionPlazo = description_plazo;
            newValue.editField = columnaNombre.nombre;
            newValue.oldValue = columnaNombre.nombre == 'Estado' ? valorAnterior == 1 ? 'Activo': valorAnterior == "" ? "":'Inactivo' : valorAnterior;
            newValue.newValue = columnaNombre.nombre == 'Estado' ? valorNuevo == 1 ? 'Activo':'Inactivo' : valorNuevo;
            nuevoObjeto.push(newValue);
          }
        }
      }
      let auxLogs: ITableContent = UMapLogToTable(
        nuevoObjeto,
        this.logsTable,
        this.records
      );
      this.logs = auxLogs;
      this.paginator.length = this.logs.content.length;
      this._paginateLogs();
      console.log(auxLogs);
      this.spinnerService.hide();
    });
  }


  filterNames(nombre) {
    const nombres = this.columnasTabla.filter(value => value.valor === nombre);
    return nombres[0];
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
  exportDataToExcel() {
    this.libranzaService.exportToExcel(this.historico, 'reporte_historico_rango_plazos');
  }

  cancelarEdicion() {
    const dialog = this.dialog.open(ConfirmModalComponent, {
      data: { title: '¿Estas seguro de salir sin guardar cambios?', isConfirm: true },
    });

    dialog.afterClosed().subscribe(res => {
      if (res) {
        this.cancelarEdicionview = false;
        this.creandoFila = false; // Cambiar el estado a false
        this.getRangosPlazoTasasFind();
      }
    });
  }

  changePage(event: PageEvent) {
    this.paginator = event;
    this._paginateLogs();
  }

  private _paginateLogs() {
    const initCut: number = this.paginator.pageSize * this.paginator.pageIndex;
    const endCut: number = initCut + this.paginator.pageSize;
    this.paginateLogs = {
      headers: [...this.logs.headers],
      content: [...this.logs.content?.slice(initCut, endCut)],
    };
  }
}
