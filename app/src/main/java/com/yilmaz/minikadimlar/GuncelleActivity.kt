package com.yilmaz.minikadimlar

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.yilmaz.minikadimlar.databinding.ActivityGuncelleBinding

class GuncelleActivity : AppCompatActivity() {
    lateinit var binding: ActivityGuncelleBinding
    private lateinit var auth: FirebaseAuth
    var databaseReference: DatabaseReference? = null
    var database: FirebaseDatabase? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityGuncelleBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseReference = database?.reference!!.child("profile")

        var currentUser = auth.currentUser
        binding.guncelleemail.setText(currentUser?.email)
        //realtime-databasedeki kullanicinin id sine erisip kullanciinin adini soyadini alalim
        var userReference = databaseReference?.child(currentUser?.uid!!)
        userReference?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                binding.guncelleadsoyad.setText(snapshot.child("adisoyadi").value.toString())
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
        //bilgilerimi guncelle button
        binding.guncellebilgilerimibutton.setOnClickListener {
            var guncelleemail = binding.guncelleemail.text.toString().trim()
            currentUser!!.updateEmail(guncelleemail)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            applicationContext,
                            "E-mailiniz guncellendi",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        Toast.makeText(applicationContext, "Islem basarisiz", Toast.LENGTH_LONG)
                            .show()
                    }
                }
            //parola guncelleme
            var guncelleparola = binding.guncelleparola.text.toString().trim()
            currentUser!!.updatePassword(guncelleparola)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            applicationContext,
                            "Parolaniz guncellendi",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        Toast.makeText(applicationContext, "Islem basarisiz", Toast.LENGTH_LONG)
                            .show()
                    }
                }
            // ad soyad guncelleme
            var currentUserDb = currentUser?.let { it1 -> databaseReference?.child(it1.uid) }
            currentUserDb?.removeValue()
            currentUserDb?.child("adisoyadi")?.setValue(binding.guncelleadsoyad.text.toString())
            Toast.makeText(applicationContext, "AdiSoyadi guncellendi", Toast.LENGTH_LONG).show()
        }
        binding.guncellegirisyapbutton.setOnClickListener {
            intent = Intent(applicationContext, GirisActivity::class.java)
            startActivity(intent)
            finish()


            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }
        }
    }
}