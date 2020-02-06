package id.infiniteuny.apps.util

import android.content.Context
import android.view.Gravity
import android.widget.Toast

fun Context.toast(msg: String) = msg.let {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}
fun Context.toastCenter(msg:String)=msg.let{
    val tst = Toast.makeText(this,msg,Toast.LENGTH_SHORT)
    tst.setGravity(Gravity.CENTER,0,0)
    tst.show()
}