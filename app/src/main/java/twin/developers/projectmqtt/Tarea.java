package twin.developers.projectmqtt;

public class Tarea {
    private String usuario;
    private String tarea;
    private String prioridad;
    private String fechaLimite;

    public Tarea(String usuario, String tarea, String prioridad, String fechaLimite) {
        this.usuario = usuario;
        this.tarea = tarea;
        this.prioridad = prioridad;
        this.fechaLimite = fechaLimite;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public void setTarea(String tarea) {
        this.tarea = tarea;
    }

    public void setPrioridad(String prioridad) {
        this.prioridad = prioridad;
    }

    public void setFechaLimite(String fechaLimite) {
        this.fechaLimite = fechaLimite;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getTarea() {
        return tarea;
    }

    public String getPrioridad() {
        return prioridad;
    }

    public String getFechaLimite() {
        return fechaLimite;
    }
}
