package bryan.miranda.ejemplospinner

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import modelo.ClaseConexion
import modelo.dataClassDoctores

class pacientes : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_pacientes,container, false)

        val spDoctores = root.findViewById<Spinner>(R.id.spDoctores)

        fun obtenerDoctores(): List<dataClassDoctores>
        {
            val objConexion = ClaseConexion().cadenaConexion()

            val statement = objConexion?.createStatement()
            val result = statement?.executeQuery("select * from tbDoctores")!!

            val listaDoctores = mutableListOf<dataClassDoctores>()

            while (result.next()) {
                val uuid = result.getString("DoctorUUID")
                val nombre = result.getString("nombreDoctor")
                val especialidad = result.getString("Especialidad")
                val telefono = result.getString("Telefono")
                val doctorCompleto = dataClassDoctores(uuid, nombre, especialidad, telefono)
                listaDoctores.add(doctorCompleto)
            }
            return listaDoctores
        }

        CoroutineScope(Dispatchers.IO).launch {
            val listadoDeDoctores = obtenerDoctores()
            val nombreDoctores = listadoDeDoctores.map {  it.nombreDoctor }

           withContext(Dispatchers.Main) {
               val miAdaptador = ArrayAdapter(
                   requireContext(),
                   android.R.layout.simple_spinner_dropdown_item,
                   nombreDoctores
               )
               spDoctores.adapter = miAdaptador
           }
        }
        return root

    }
}