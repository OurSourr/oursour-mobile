
package cl.oursourmobile.data.autenticacion

object AutorizacionDemo {

    fun esAdministrador(): Boolean {
        val usuario = AutenticacionDemo.repository.obtenerUsuarioActual()

        return usuario?.rol == RolUsuario.ADMIN
    }

    fun puedeAdministrarProductos(): Boolean {
        return esAdministrador()
    }

    fun puedeAdministrarRecetas(): Boolean {
        return esAdministrador()
    }
}
