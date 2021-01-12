package es.eoi.supertaskag.start

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import es.eoi.supertaskag.R
import es.eoi.supertaskag.home.HomeActivity
import es.eoi.supertaskag.models.User
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    val TAG = "miapp"
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        supportActionBar?.title = "Registro"

        // Inicializamos Firebase con "Authentication"
        auth = Firebase.auth
        // Inicializamos "Cloud Firestore"
        db = Firebase.firestore

        btnSignUp.setOnClickListener {
            if (etPass.text.toString().length > 6) {
                registerUser(
                    etEmail.text.toString(),
                    etPass.text.toString(),
                    etName.text.toString()
                )
            } else Toast.makeText(this, "Introduce una contraseña > 6 caracteres", Toast.LENGTH_LONG).show()
        }
    }

    // 1. REGISTRO EN FIREBASE AUTHENTICATION
    fun registerUser(email: String, password: String, name: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                    // Si el registro ha ido bien...
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser

                    createUserInFirestore(user!!.uid, name, email)

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                // ...
            }

    }

    // 2. REGISTRO EN FIRESTORE
    fun createUserInFirestore(uid: String, nombre: String, correo: String) {

        val newUser = User()
        newUser.name = nombre
        newUser.email = correo

        db.collection("users").document(uid)
            .set(newUser)
            .addOnSuccessListener { document ->
                Log.v(TAG, "createUserInFirestore:TODOFENOMENAL")
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            }
            .addOnFailureListener { e ->
                Log.v(TAG, "createUserInFirestore:failure", e)
            }
    }

}