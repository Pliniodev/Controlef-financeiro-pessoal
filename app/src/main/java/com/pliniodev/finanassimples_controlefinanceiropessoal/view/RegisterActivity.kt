package com.pliniodev.finanassimples_controlefinanceiropessoal.view

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.pliniodev.finanassimples_controlefinanceiropessoal.R
import com.pliniodev.finanassimples_controlefinanceiropessoal.databinding.ActivityRegisterBinding
import com.pliniodev.finanassimples_controlefinanceiropessoal.service.constants.TransactionConstants.Companion.TRANSACTIONID
import com.pliniodev.finanassimples_controlefinanceiropessoal.service.model.TransactionModel
import com.pliniodev.finanassimples_controlefinanceiropessoal.viewmodel.RegisterViewModel
import java.text.SimpleDateFormat
import java.util.*

class RegisterActivity : AppCompatActivity(), View.OnClickListener {
    /**
     * Activitie onde é feito o registro de despesas e receitas do usuário
     */

    private lateinit var binding: ActivityRegisterBinding

    private lateinit var mViewModel: RegisterViewModel
    private var mTransactionId: Int = 0

//    private lateinit var transaction: TransactionModel

    private var mCalendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mViewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)

        setListeners()
        observe()
        loadData()

        binding.switchPay.isChecked = false
        binding.radioExpense.isChecked = true//default value for transactionType


    }

    private fun loadData() {
        val bundle = intent.extras
        if (bundle != null) {
            mTransactionId = bundle.getInt(TRANSACTIONID)
            mViewModel.load(mTransactionId)
        }
    }

    private fun observe() {
        mViewModel.saveTransaction.observe(this, Observer{
            if (it){
                Toast.makeText(applicationContext, "Sucesso!", Toast.LENGTH_SHORT).show()

            } else {
                Toast.makeText(applicationContext, "Falha!", Toast.LENGTH_SHORT).show()
            }
            finish()
        })

        mViewModel.transaction.observe(this, Observer {
//            edit_name.setText(it.name)
//            if(it.presence) {
//                radio_presence.isChecked = true
//            } else {
//                radio_absent.isChecked = true
//            }
        })
    }

    private fun setListeners() {
        binding.editDate.setOnClickListener(this)
        binding.switchPay.setOnClickListener(this)
        binding.buttonSave.setOnClickListener(this)
    }

    private fun updateDateInView() {
        val myFormat = "dd/MM/yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        val date = sdf.format(mCalendar.time)
        binding.editDate.setText(date.toString())
    }

    override fun onClick(view: View) {

        val dateSetListener =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                mCalendar.set(Calendar.YEAR, year)
                mCalendar.set(Calendar.MONTH, monthOfYear)
                mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInView()
            }

        val id = view.id
        if (id == R.id.edit_date) {

            DatePickerDialog(
                this, dateSetListener,
                mCalendar.get(Calendar.YEAR),
                mCalendar.get(Calendar.MONTH),
                mCalendar.get(Calendar.DAY_OF_MONTH)
            ).show()

        } else if (id == R.id.switch_pay) {
            if (binding.switchPay.isChecked){
                binding.switchPay.text = "Pago"
            } else{
                binding.switchPay.text = "À Pagar"
            }
        } else if (id == R.id.button_save) {
            val transactionType = binding.radioExpense.isChecked
            val name = binding.editName.text.toString()
            val description = binding.editDescription.text.toString()
            val price = binding.editPrice.text.toString().toDouble()
            val category = binding.editCategory.text.toString()
            val dueDate = binding.editDate.text.toString()
            val paidOut = binding.switchPay.isChecked
            val observation = binding.observationExtra.toString()

            mViewModel.save(mTransactionId, transactionType, name, description, price, category,
                    dueDate, paidOut, observation)
        }

    }
}



//        //binding fragment

//        //binding activity


