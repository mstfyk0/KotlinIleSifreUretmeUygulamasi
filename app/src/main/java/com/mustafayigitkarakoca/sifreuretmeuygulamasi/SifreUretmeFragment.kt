package com.mustafayigitkarakoca.sifreuretmeuygulamasi

import android.app.AlertDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_sifre_uretme.*
import kotlin.random.Random


class SifreUretmeFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onStart() {
        super.onStart()
    }
    override fun onResume() {
        super.onResume()
    }
    override fun onPause() {
        super.onPause()
    }
    override fun onStop() {
        super.onStop()
    }
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_sifre_uretme, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        butonSifreUret.setOnClickListener {
            SifreUret(it)
        }
        butonKaydet.setOnClickListener {
            sifreKaydet(it)
        }
        textViewOlusturulanSifre.setOnClickListener {
            kopyala(it)
        }
        butonUpdate.setOnClickListener {
            update(it)
        }
        arguments?.let {
            val gelenBilgi = SifreUretmeFragmentArgs.fromBundle(it).bilgi
            if (gelenBilgi.equals("menudengeldim")){
                editTextSifreNereninSifresi.text.clear()
                textViewOlusturulanSifre.text=""
            } else {
                butonUpdate.visibility=View.VISIBLE
                butonKaydet.visibility=View.INVISIBLE
                val secilenId= SifreUretmeFragmentArgs.fromBundle(it).id
                try {
                    context?.let {
                        val db =it.openOrCreateDatabase("Sifreler",Context.MODE_PRIVATE,null)
                        val cursor = db.rawQuery("SELECT * FROM sifreler WHERE id =?", arrayOf(secilenId.toString()))
                        val sifreNereninSifresi = cursor.getColumnIndex("sifreninkaydi")
                        //val sifre = cursor.getColumnIndex("sifre")
                        while (cursor.moveToNext()){
                            //textViewOlusturulanSifre.setText(cursor.getString(sifre))
                            editTextSifreNereninSifresi.setText(cursor.getString(sifreNereninSifresi))
                        }
                        cursor.close()
                    }
                }catch (e:Exception){
                    e.printStackTrace()
                }
            }
        }
    }
    fun SifreUret(view: View) {
        val editTextDegeri = editTextSifreUzunlugu.text.toString()
        if (editTextDegeri == "") {
            Toast.makeText(context, "LÜTFEN İSTEDİĞİNİZ UZUNLUĞU GİRİNİZ ", Toast.LENGTH_LONG).show()
        } else {
            val editTextenGelenSifreUzunluk = editTextDegeri.toInt()
            if (editTextenGelenSifreUzunluk < 10) {
                Toast.makeText(context, "LÜTFEN ŞİFRE UZUNLUĞUNU 10 DAN FAZLA SEÇİN", Toast.LENGTH_LONG).show()
            } else {
                var bosString: String = ""
                val randomOlusturulanSayilar = List(editTextenGelenSifreUzunluk) { Random.nextInt(32, 126).toChar() }
                //println(randomOlusturulanSayilar)
                for (i in randomOlusturulanSayilar) {
                    bosString += i
                }
                textViewOlusturulanSifre.text = "${bosString}"
                butonSifreUret.hideKeyboard()
            }
        }
    }
    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    fun kopyala(view: View) {
        if(textViewOlusturulanSifre.text==""){
            Toast.makeText(context, "LÜTFEN ÖNCE ŞİFRE OLUŞTURUN", Toast.LENGTH_LONG).show()
        }else{
            context?.let {
                val clipboard = getSystemService(it, ClipboardManager::class.java) as ClipboardManager
                val textToCopy = textViewOlusturulanSifre.text
                val clip = ClipData.newPlainText("", textToCopy)
                clipboard.setPrimaryClip(clip)
                Toast.makeText(it, "ŞİFRE KOPYALANDI", Toast.LENGTH_LONG).show()
                //println(clip)
            }
        }
    }
    fun sifreKaydet(view: View) {
        val kaydetmekIcinGelenSifre = textViewOlusturulanSifre.text.toString()
        val sifreninKayitEdilecegiYer = editTextSifreNereninSifresi.text.toString()
        if (kaydetmekIcinGelenSifre != "") {
            if (sifreninKayitEdilecegiYer != "") {
                try {
                    context?.let {
                        val database = it.openOrCreateDatabase("Sifreler", Context.MODE_PRIVATE, null)
                        database.execSQL("CREATE TABLE IF NOT EXISTS sifreler (id INTEGER PRIMARY KEY, sifreninkaydi VARCHAR, sifre VARCHAR)")
                        val sqlString = "INSERT INTO sifreler (sifreninkaydi,sifre) VALUES (?,?)"
                        //database.execSQL("INSERT INTO sifreler (sifreninkaydi,sifre) VALUES ('instagram','asdfasdfasdf') ")
                        val statement = database.compileStatement(sqlString)
                        statement.bindString(1, sifreninKayitEdilecegiYer)
                        statement.bindString(2, kaydetmekIcinGelenSifre)
                        statement.execute()
                        sifreUretmeVeKaydetmeyeDevammi(view)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else {
                Toast.makeText(context, "LÜTFEN ŞİFRENİN NERENİN ŞİFRESİ OLDUĞUNU YAZINIZ", Toast.LENGTH_LONG, ).show()
            }
        } else {
            Toast.makeText(context, "LÜTFEN ÖNCE ŞİFRE OLUŞTURUN", Toast.LENGTH_LONG, ).show()
        }
    }
    fun sifreUretmeVeKaydetmeyeDevammi(view: View){
        val uyariMesaji = AlertDialog.Builder(context)
        uyariMesaji.setTitle("DEVAM EDİLSİN Mİ?")
        uyariMesaji.setMessage("ŞİFRE ÜRETMEYE, KAYIT ETMEYE DEVAM EDECEK MİSİNİZ?")
        uyariMesaji.setPositiveButton("EVET",DialogInterface.OnClickListener { dialog, i ->
            editTextSifreUzunlugu.text.clear()
            editTextSifreNereninSifresi.text.clear()
            textViewOlusturulanSifre.text =""
        })
        uyariMesaji.setNegativeButton("HAYIR",DialogInterface.OnClickListener { dialog, i ->
            val action = SifreUretmeFragmentDirections.actionSifreUretmeFragmentToKaydedilenSifreler()
            Navigation.findNavController(view).navigate(action)
        })
        uyariMesaji.show()
    }
    fun update(view: View){
        val kaydetmekIcinGelenSifre = textViewOlusturulanSifre.text.toString()
        val sifreninKayitEdilecegiYer = editTextSifreNereninSifresi.text.toString()
        arguments?.let {
            val gelenbilgi = SifreUretmeFragmentArgs.fromBundle(it).bilgi
            val gelenId = SifreUretmeFragmentArgs.fromBundle(it).id

            if (gelenbilgi.equals("recyclerdangeldim")){
                if (kaydetmekIcinGelenSifre != "") {
                    if (sifreninKayitEdilecegiYer != "") {
                        context?.let {
                            try {
                                val updateDatabase = it.openOrCreateDatabase("Sifreler", Context.MODE_PRIVATE, null)
                                val sqlStringUpdate = "UPDATE sifreler SET sifreninkaydi=?,sifre=? WHERE id=?"
                                val statementUpdate = updateDatabase.compileStatement(sqlStringUpdate)
                                statementUpdate.bindString(1, sifreninKayitEdilecegiYer)
                                statementUpdate.bindString(2, kaydetmekIcinGelenSifre)
                                statementUpdate.bindString(3,gelenId.toString())
                                statementUpdate.execute()
                                val action = SifreUretmeFragmentDirections.actionSifreUretmeFragmentToKaydedilenSifreler()
                                Navigation.findNavController(view).navigate(action)
                                butonUpdate.visibility=View.INVISIBLE
                                butonKaydet.visibility=View.VISIBLE
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    } else {
                        Toast.makeText(context, "LÜTFEN ŞİFRENİN NERENİN ŞİFRESİ OLDUĞUNU YAZINIZ", Toast.LENGTH_LONG, ).show()
                    }
                }else{
                    Toast.makeText(context, "LÜTFEN ÖNCE ŞİFRE OLUŞTURUN", Toast.LENGTH_LONG, ).show()
                }
            }
        }
    }
}
