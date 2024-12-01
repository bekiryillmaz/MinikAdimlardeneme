package com.yilmaz.minikadimlar

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.yilmaz.minikadimlar.databinding.ActivityPsifirlaBinding

class PsifirlaActivity : AppCompatActivity() {
    lateinit var binding: ActivityPsifirlaBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityPsifirlaBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        binding.psifirlamabutton.setOnClickListener {
            var psifirlaemail = binding.psifirlaemail.text.toString().trim()
            if (TextUtils.isEmpty(psifirlaemail)) {                              //email kismi bos ise kullanciiya uyari versin
                binding.psifirlaemail.error = "Lutfen e-mail adresininizi yaziniz"
            } else {
                auth.sendPasswordResetEmail(psifirlaemail)
                    .addOnCompleteListener(this) { sifirlama ->
                        if (sifirlama.isSuccessful) {
                            binding.psifirlamesaj2.text =
                                "Kayitli e-mail adresinize sifirlama baglantisi gonderildi,Lutfen kontrol ediniz"
                        } else {
                            binding.psifirlamesaj2.text = "Sifirlama isleminiz basarisiz"
                        }

                    }
            }
        }
        //Giris sayfasina gitmek icin
        binding.psifirlamagirisyapbutton.setOnClickListener {
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