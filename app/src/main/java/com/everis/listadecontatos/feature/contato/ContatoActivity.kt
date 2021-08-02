package com.everis.listadecontatos.feature.contato

import android.os.Bundle
import android.util.Log
import android.view.View
import com.everis.listadecontatos.R
import com.everis.listadecontatos.application.ContatoApplication
import com.everis.listadecontatos.bases.BaseActivity
import com.everis.listadecontatos.feature.listacontatos.model.ContatosVO
import com.everis.listadecontatos.singleton.ContatoSingleton
import kotlinx.android.synthetic.main.activity_contato.*
import kotlinx.android.synthetic.main.activity_contato.toolBar


class ContatoActivity : BaseActivity() {

    private var index = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contato)
        setupToolBar(toolBar, "Contato",true)
        setupContato()
        btnSalvarConato.setOnClickListener { onClickSalvarContato() }
    }

    private fun setupContato(){

        if(intent.getSerializableExtra("index") != null){
            index = intent.getSerializableExtra("index").toString().toInt()
        }
        if (index == -1 ){
            btnExcluirContato.visibility = View.GONE
            return
        }

        var contato = ContatoApplication.instance.helperDB?.buscarContatos("$index",true) ?: return
        var c = contato.getOrNull(0)?:return
        Log.d("TESTE", c.toString())
        etNome.setText(c.nome)
        etTelefone.setText(c.telefone)
    }

    private fun onClickSalvarContato(){
        val nome = etNome.text.toString()
        val telefone = etTelefone.text.toString()
        val contato = ContatosVO(
            null,
            nome,
            telefone
        )
        ContatoApplication.instance.helperDB?.salvarContato(contato)
        finish()
    }

    fun onClickExcluirContato(view: View) {
        ContatoApplication.instance.helperDB?.deleteContato(index.toString())
        finish()

    }
}