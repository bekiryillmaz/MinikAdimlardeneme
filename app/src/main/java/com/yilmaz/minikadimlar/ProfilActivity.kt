package com.yilmaz.minikadimlar

import android.annotation.SuppressLint
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
import com.yilmaz.minikadimlar.databinding.ActivityProfilBinding

class ProfilActivity : AppCompatActivity() {
    lateinit var binding:ActivityProfilBinding
    private lateinit var auth: FirebaseAuth
    var databaseReference: DatabaseReference?=null
    var database: FirebaseDatabase?=null
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityProfilBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()



        database = FirebaseDatabase.getInstance()
        databaseReference = database?.reference!!.child("profile")
        var currentUser = auth.currentUser
        binding.profilemail.text = "E-mail: "+currentUser?.email

        //realtime-database deki id ye ulasip altindaki childlarin icindeki veriyi sayfaya aktaricaz
        var userReference = databaseReference?.child(currentUser?.uid!!)
        userReference?.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                binding.profiladsoyad.text = "Tam adiniz: "+ snapshot.child("adisoyadi").value.toString() // baska bilgileri almak sitiyorsak ctrl d yapip ahngi bilgiyse onunla degistirmemiz lazim

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //cikis yap butonu
        binding.profilcikisyapbutton.setOnClickListener {
            auth.signOut()   //oturumu sonlandirdik
            startActivity(Intent(this@ProfilActivity,GirisActivity::class.java))    // oturumu sonlandirdigimizda giris sayfasina yonlendirdik
            finish()  // giris sayfasina gittigimizde profil sayfamizin sonlanmasini istiyoruz
        }
        //hesabimi sil butonu
        binding.profilhesabimisilbutton.setOnClickListener {
            currentUser?.delete()?.addOnCompleteListener{
                if (it.isSuccessful){
                    Toast.makeText(applicationContext,"Hesabiniz Silindi",Toast.LENGTH_LONG).show()
                    auth.signOut()
                    startActivity(Intent(this@ProfilActivity,GirisActivity::class.java))
                    finish()
                    var currentUserDb = currentUser?.let { it1 -> databaseReference?.child(it1.uid) }
                    currentUserDb?.removeValue()
                    Toast.makeText(applicationContext,"AdiSoyadi Silindi",Toast.LENGTH_LONG).show()
                }
            }
        }

        //guncelle butonu
        binding.profilbilgiguncellemebutton.setOnClickListener {
            startActivity(Intent(this@ProfilActivity,GuncelleActivity::class.java))    // oturumu sonlandirdigimizda giris sayfasina yonlendirdik
            finish()  // giris sayfasina gittigimizde profil sayfamizin sonlanmasini istiyoruz
        }
    }
}