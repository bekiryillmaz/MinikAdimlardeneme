package com.yilmaz.minikadimlar

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.yilmaz.minikadimlar.databinding.ActivityUyeBinding

class UyeActivity : AppCompatActivity() {
    lateinit var binding: ActivityUyeBinding
    private lateinit var auth: FirebaseAuth
    var databaseReference:DatabaseReference?=null
    var database:FirebaseDatabase?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUyeBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseReference = database?.reference!!.child("profile")

        //Kaydet butonu ile kaydetme adimlari
        binding.uyekaydetbutton.setOnClickListener{
            var uyeadsoyad = binding.uyeadsoyad.text.toString()
            var uyeemail = binding.uyeemail.text.toString()
            var uyeparola = binding.uyeparola.text.toString()
            if (TextUtils.isEmpty(uyeadsoyad)){
                binding.uyeadsoyad.error = "Lutfen adinizi ve soyadinizi giriniz"
                return@setOnClickListener
            }else if (TextUtils.isEmpty(uyeemail)){
                binding.uyeemail.error = "Lutfen e-mail adresinizi giriniz"
                return@setOnClickListener
            }else if (TextUtils.isEmpty(uyeparola)){
                binding.uyeparola.error = "Lutfen parolanizi giriniz"
                return@setOnClickListener
            }

            // e-mail,parola ve kullanici bilgilerini veri tabanina ekleme
            auth.createUserWithEmailAndPassword(binding.uyeemail.text.toString(),binding.uyeparola.text.toString())
                .addOnCompleteListener(this){ task ->
                    if (task.isSuccessful) {
                        //suanki kullanici bilgilerini alalim
                        var currentUser = auth.currentUser
                        //Kullanici id sini alip o id adi altinda adimizi ve soyadimizi kaydedelim
                        var currentUserDb = currentUser?.let { it1 -> databaseReference?.child(it1.uid) }
                        currentUserDb?.child("adisoyadi")?.setValue(binding.uyeadsoyad.text.toString())
                        Toast.makeText(this@UyeActivity, "Kayit Basarili",Toast.LENGTH_LONG).show()
                    }else{
                        Toast.makeText(this@UyeActivity, "Kayit Hatali",Toast.LENGTH_LONG).show()
                    }
                }
        }
        //Giris sayfasina gitmek icin
        binding.uyegirisyapbutton.setOnClickListener{
            intent = Intent(applicationContext,GirisActivity::class.java)
            startActivity(intent)
            finish()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}