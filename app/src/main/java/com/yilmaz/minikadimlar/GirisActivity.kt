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
import com.yilmaz.minikadimlar.databinding.ActivityGirisBinding

class GirisActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    lateinit var binding: ActivityGirisBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityGirisBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        // kullanicinin oturup acip acmadigini kontrol edelim
        var currentUser = auth.currentUser
        if (currentUser != null) {
            startActivity(Intent(this@GirisActivity, ProfilActivity::class.java))
            finish()
        }
        // giris yap butonuna tiklandiginda
        binding.girisyapbutton.setOnClickListener {
            var girisemail = binding.girisemail.text.toString()
            var girisparola = binding.girisparola.text.toString()
            if (TextUtils.isEmpty(girisemail)) {
                binding.girisemail.error = "Lutfen e-mail adresinizi giriniz"
                return@setOnClickListener
            } else if (TextUtils.isEmpty(girisparola)) {
                binding.girisparola.error = "Lutfen parolanizi giriniz"
                return@setOnClickListener
            }
            // giris bilgilerimizi dogrulayip giris yapiyoruz
            auth.signInWithEmailAndPassword(girisemail,girisparola)
                .addOnCompleteListener(this){
                    if (it.isSuccessful){
                        intent = Intent(applicationContext, ProfilActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else{ Toast.makeText(applicationContext,"Giris hatali,lutfen tekrar deneyiniz"
                        ,Toast.LENGTH_LONG).show()
                    }
                }

        }
        //yeni uyelik sayfasina gitmek icin
        binding.girisyeniuyelik.setOnClickListener {
            intent = Intent(applicationContext,UyeActivity::class.java)
            startActivity(intent)
            finish()
        }

        //parolami unuttum sayfasina gitmek icin
        binding.girisparolaunuttum.setOnClickListener {
            intent = Intent(applicationContext,PsifirlaActivity::class.java)
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