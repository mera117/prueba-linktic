
export interface SeguroVoluntario{
    id: number;
    nombre_plan: String;
    valor_prima: String;
    fallecimiento_accidental:String;
    inhabilitacion_total:String;
    enfermedades_graves: String;
    canasta_fallecimiento: String;
    estado: number;
    asalariado: String;
    independiente: String;
    pensionado: String;
    edad_minima: number;
    edad_minima_dias: number;
    edad_maxima: number;
    edad_maxima_dias: number;
    usuario: String;
    fecha_hora: String, 
    ip:String,
    editando?: boolean;
   }