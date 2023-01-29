package com.example.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.TextKeyListener.clear
import android.view.View
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var one:Button
    private lateinit var two:Button
    private lateinit var three:Button
    private lateinit var four:Button
    private lateinit var five:Button
    private lateinit var six:Button
    private lateinit var seven:Button
    private lateinit var eight:Button
    private lateinit var nine:Button
    private lateinit var zero:Button

    private lateinit var point:Button
    private lateinit var clear:Button
    private lateinit var delete:Button
    private lateinit var percent:Button

    private lateinit var div: Button
    private lateinit var multiply: Button
    private lateinit var plus: Button
    private lateinit var minus: Button


    private lateinit var oper: TextView
    private lateinit var result: TextView

    private var isPoint = true
    private var isSymbol = false
    private var isPercent = false
//    private var isPoint = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initUI()
        one.setOnClickListener(this)
        two.setOnClickListener(this)
        three.setOnClickListener(this)
        four.setOnClickListener(this)
        five.setOnClickListener(this)
        six.setOnClickListener(this)
        seven.setOnClickListener(this)
        eight.setOnClickListener(this)
        nine.setOnClickListener(this)
        zero.setOnClickListener(this)

        point.setOnClickListener{
            if (isPoint && oper.text[oper.text.length-1].isDigit()){
                oper.text = oper.text.toString() + "."
                isPoint = false
                isSymbol = false
                isPercent = false
            }
        }

        percent.setOnClickListener {
            if (isPercent) {
                oper.text = oper.text.toString() + "%"
                isPercent = false
                isSymbol = false
            }
        }
        clear.setOnClickListener { clear() }

        delete.setOnClickListener{
            if (oper.text.length == 1){
                clear()
            }else{
                if (oper.text[oper.text.length-1] == '.'){
                    isPoint = true
                }
                if (!oper.text[oper.text.length - 1].isDigit()) {
                    isSymbol = true
                    isPercent = true
                }
                oper.text = oper.text.substring(0, oper.text.length - 1)
            }
            var str: String = oper.text.toString()

            if (!oper.text[oper.text.length - 1].isDigit()) {
                oper.text = oper.text.substring(0, oper.text.length - 1)
                result.text = calculate()
                oper.text = str
            } else {
                result.text = calculate()
            }
        }
        div.setOnClickListener { addSymbol(div.text.toString()) }
        percent.setOnClickListener { addSymbol(percent.text.toString()) }
        multiply.setOnClickListener { addSymbol(multiply.text.toString()) }
        plus.setOnClickListener { addSymbol(plus.text.toString()) }
        minus.setOnClickListener { addSymbol(minus.text.toString()) }
    }



    private fun initUI(){
        one = findViewById(R.id.btnOne)
        two = findViewById(R.id.btnTwo)
        three = findViewById(R.id.btnThree)
        four = findViewById(R.id.btnFour)
        five = findViewById(R.id.btnFive)
        six = findViewById(R.id.btnSix)
        seven = findViewById(R.id.btnSeven)
        eight = findViewById(R.id.btnEight)
        nine = findViewById(R.id.btnNine)
        zero = findViewById(R.id.btnZero)

        point = findViewById(R.id.btnPeriod)
        clear = findViewById(R.id.btnC)
        delete = findViewById(R.id.btnDelete)
        percent = findViewById(R.id.btnPercent)

        div = findViewById(R.id.btnDivide)
        multiply = findViewById(R.id.btnMultiply)
        plus = findViewById(R.id.btnPlus)
        minus = findViewById(R.id.btnMinus)

        oper = findViewById(R.id.oper)
        result = findViewById(R.id.result)

    }

    override fun onClick(p0: View?) {
        val btn = findViewById<Button>(p0!!.id)
        if (oper.text != "0"){
            oper.text = oper.text.toString() + btn.text
        }else{
            oper.text = btn.text
        }
        result.text = calculate()
        isSymbol = true
        isPercent = true
    }
    private fun calculate():String{
        var res: Any = "0"
        var myList: MutableList<Any> = createArray(oper.text.toString())
        var p = 0
        var m = 0

        while (p<myList.size){
            if (myList[p].toString() == "%"){
                res = (myList[p-1].toString().toDouble() / 100 * (myList[p+1].toString().toDouble()))
                replace(p,myList)
                myList.add(p-1,res)
                p-=2
            }
            p++
        }

        if (myList.size>=3 && myList.size %2 !=0){
            while (m<myList.size){
                if ((myList[m].toString() == "×") || (myList[m].toString() == "÷")){
                    if (myList[m].toString() == "×") {
                        res = myList[m - 1].toString().toDouble() * myList[m + 1].toString()
                            .toDouble()
                        replace(m, myList)
                        myList.add(m - 1, res)
                        m -= 2
                    }else{
                        res = myList[m-1].toString().toDouble() / myList[m+1].toString().toDouble()
                        replace(m,myList)
                        myList.add(m-1,res)
                        m-=2
                    }
                }
                m++
            }
        }
        return if (res.toString().toDouble() - res.toString().toDouble().toInt() == 0.0){
            (res.toString().toDouble()).toInt().toString()
        } else {
            res.toString()
        }
    }
    private fun clear() {
        oper.text = "0"
        result.text = "0"
        isPoint = true
        isSymbol = false
        isPercent = false
    }

    private fun addSymbol(symbol:String){
        if(isSymbol && oper.text[oper.text.length-1] != '.'){
            oper.text = oper.text.toString() + symbol
            isSymbol = false
            isPercent = false
        }else{
            if (oper.text != "0" && oper.text[oper.text.length - 1] != '.') {
                oper.text = oper.text.substring(0, oper.text.length - 1) + symbol
            }
        }
        isPoint = true
    }


    private fun createArray(operText:String):MutableList<Any>{
        var list  = mutableListOf<Any>()
        var temp = ""
        for (i in operText){
            if (i.isDigit() || i == '.'){
                temp += i
            }else{
                list.add(temp)
                list.add(i)
                temp = ""
            }
        }
        if (temp.isNotEmpty()) {
            list.add(temp)
        }
        return list
    }
    private fun replace(i: Int, myList: MutableList<Any>) {
        myList.removeAt(i)
        myList.removeAt(i)
        myList.removeAt(i - 1)
    }
}