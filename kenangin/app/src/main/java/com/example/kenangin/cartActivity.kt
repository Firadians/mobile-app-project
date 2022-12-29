package com.example.kenangin

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.kenangin.adapter.MyDrinkAdapter
import com.example.kenangin.eventbus.UpdateCartEvent
import com.example.kenangin.listener.ICartLoadListerner
import com.example.kenangin.listener.IDrinkLoadListener
import com.example.kenangin.model.CartModel
import com.example.kenangin.model.DrinkModel
import com.example.kenangin.utils.SpaceItemDecoration
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_home.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class cartActivity : AppCompatActivity(), IDrinkLoadListener, ICartLoadListerner {

    lateinit var drinkLoadListener: IDrinkLoadListener
    lateinit var cartLoadListerner: ICartLoadListerner
    val database = Firebase.database
    private lateinit var auth: FirebaseAuth


    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)

    }

    override fun onStop() {
        super.onStop()
        if(EventBus.getDefault().hasSubscriberForEvent(UpdateCartEvent::class.java))
            EventBus.getDefault().removeStickyEvent(UpdateCartEvent::class.java)
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public fun onUpdateCartEvent(event: UpdateCartEvent){
        countCartFromFirebase()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        init()
        loadDrinkFromFirebase()
        countCartFromFirebase()
    }

    private fun countCartFromFirebase() {
        val cartModels : MutableList<CartModel> = ArrayList()
        FirebaseDatabase.getInstance()
            .getReference("Cart")
            .child("UNIQUE_USER_ID")
            .addListenerForSingleValueEvent(object:ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    for(cartSnapshot in snapshot.children){
                        val cartModel = cartSnapshot.getValue(CartModel::class.java)
                        cartModel!!.key = cartSnapshot.key
                        cartModels.add(cartModel)
                    }
                    cartLoadListerner.onLoadCartSuccess(cartModels)
                }

                override fun onCancelled(error: DatabaseError) {
                    cartLoadListerner.onLoadCartFailed(error.message)
                }

            })
    }

    private fun loadDrinkFromFirebase() {
        var drinkModels : MutableList<DrinkModel> = ArrayList()
        FirebaseDatabase.getInstance()
            .getReference("Drink")
            .addListenerForSingleValueEvent(object: ValueEventListener{
                override fun onDataChange(snapshot :DataSnapshot){
                    if(snapshot.exists())
                    {
                        for(drinkSnapshot in snapshot.children){
                            val drinkModel = drinkSnapshot.getValue(DrinkModel::class.java)
                            drinkModel!!.key = drinkSnapshot.key
                            drinkModels.add(drinkModel)
                        }
                        drinkLoadListener.onDrinkLoadSuccess(drinkModels)
                    }else{
                        drinkLoadListener.onDrinkLoadFailed("Drink Item not Exists")
                    }
                }

                override fun onCancelled(error: DatabaseError){
                    drinkLoadListener.onDrinkLoadFailed(error.message)
                }
            })
    }

    private fun init() {
        drinkLoadListener = this
        cartLoadListerner = this

        val gridLayoutManager = GridLayoutManager(this,2)

        recycler_drink.layoutManager = gridLayoutManager
        recycler_drink.addItemDecoration( SpaceItemDecoration())

        btnCart.setOnClickListener{startActivity(Intent(this,AuthPage::class.java))}
    }


    override fun onDrinkLoadFailed(message: String?) {
        Snackbar.make(mainLayout,message!!,Snackbar.LENGTH_LONG).show()
    }

    override fun onDrinkLoadSuccess(drinkModelList: List<DrinkModel>?) {
        val adapter = MyDrinkAdapter(this,drinkModelList!!,cartLoadListerner)
        recycler_drink.adapter = adapter
    }

    override fun onLoadCartSuccess(cartModelList: List<CartModel>) {
        var cartSum = 0
        for(cartModel in cartModelList!!) cartSum+= cartModel!!.quantity
        //badge!!.setNumber(cartSum)
    }

    override fun onLoadCartFailed(message: String?) {
        Snackbar.make(mainLayout,message!!,Snackbar.LENGTH_LONG).show()
    }


}