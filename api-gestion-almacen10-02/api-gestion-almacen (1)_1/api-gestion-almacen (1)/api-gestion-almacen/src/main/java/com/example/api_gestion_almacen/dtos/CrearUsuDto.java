package com.example.api_gestion_almacen.dtos;


public class CrearUsuDto {
	
	private String nombreCompleto;
    private String movil;
    private String correoElectronico;
    private Long rolId;
    private String	contrasena;
    private byte[] foto;
	private boolean correoConfirmado; 
	private boolean google;
	
    public CrearUsuDto() {
    }

	public CrearUsuDto(String nombreCompleto, String movil, String correoElectronico, Long rolId, String contrasena, byte[] foto, boolean correoConfirmado) {
		this.nombreCompleto = nombreCompleto;
		this.movil = movil;
		this.correoElectronico = correoElectronico;
		this.rolId = rolId;
		this.contrasena = contrasena;
		this.foto = foto;
		this.correoConfirmado = correoConfirmado;
	}


    public String getNombreCompleto() {
		return nombreCompleto;
	}
	public void setNombreCompleto(String nombreCompleto) {
		this.nombreCompleto = nombreCompleto;
	}
	public String getMovil() {
		return movil;
	}
	public void setMovil(String movil) {
		this.movil = movil;
	}
	public String getCorreoElectronico() {
		return correoElectronico;
	}
	public void setCorreoElectronico(String correoElectronico) {
		this.correoElectronico = correoElectronico;
	}
	public Long getRolId() {
		return rolId;
	}
	public void setRolId(Long rolId) {
		this.rolId = rolId;
	}
	public String getContrasena() {
		return contrasena;
	}
	public void setContrasena(String contrasena) {
		this.contrasena = contrasena;
	}
	public byte[] getFoto() {
		return foto;
	}
	public void setFoto(byte[] foto) {
		this.foto = foto;
	}
	public boolean isCorreoConfirmado() {
		return correoConfirmado;
	}
	public void setCorreoConfirmado(boolean correoConfirmado) {
		this.correoConfirmado = correoConfirmado;
	}
	public boolean isGoogle() {
		return google;
	}
	public void setGoogle(boolean google) {
		this.google = google;
	}
	
	@Override
	public String toString() {
		return "CrearUsuDto{" +
			"nombreCompleto='" + nombreCompleto + '\'' +
			", movil='" + movil + '\'' +
			", correoElectronico='" + correoElectronico + '\'' +
			", rolId=" + rolId +
			", contrasena='[PROTECTED]'" +
			", foto=" + (foto != null ? "presente" : "null") +
			'}';
	}
}
