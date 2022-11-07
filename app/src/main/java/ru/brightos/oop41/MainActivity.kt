package ru.brightos.oop41

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewManager
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.BaseTransientBottomBar
import ru.brightos.oop41.databinding.ActivityMainBinding
import ru.brightos.oop41.utils.dp
import ru.brightos.oop41.utils.extendedListOf
import ru.brightos.oop41.view.CCircleView
import ru.brightos.oop41.view.EmojiView
import ru.brightos.oop41.view.MyCoordinatorLayout

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val circlesList = extendedListOf<CCircleView>()

        binding.root.setOnTouchAction { motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                val radius = 30.dp
                val layoutParams = CoordinatorLayout.LayoutParams(
                    (radius * 2).toInt(),
                    (radius * 2).toInt()
                ).apply {
                    setMargins(
                        (motionEvent.x - radius).toInt(),
                        (motionEvent.y - radius).toInt(),
                        0,
                        0
                    )
                }
                val newCircle = CCircleView(this, null, radius) {
                    if (it.isSelected)
                        it.isSelected = true
                }.apply {
                    this.layoutParams = layoutParams
                }
                circlesList.add(newCircle)
                binding.root.addView(newCircle)
//                binding.root.addView(EmojiView(this))
                println(binding.root.right)
            }
            true
        }

        binding.modeSwitch.setOnClickListener {
            binding.root.onTouchEnabled = !binding.root.onTouchEnabled

            binding.modeSwitch.setImageDrawable(
                ContextCompat.getDrawable(
                    this,
                    if (binding.root.onTouchEnabled)
                        R.drawable.ic_check_box
                    else
                        R.drawable.ic_add
                )
            )

            Snackbar.make(
                it,
                if (binding.root.onTouchEnabled)
                    "Включён режим создания объектов"
                else
                    "Включён режим выделения объектов",
                Snackbar.LENGTH_LONG
            ).setAnchorView(R.id.mode_switch).show()
        }

        binding.delete.setOnClickListener {
            circlesList.forEach {
                (it.parent as ViewManager).removeView(it)
            }
            circlesList.clear()

            Snackbar.make(it, "Объекты удалены", Snackbar.LENGTH_LONG)
                .setAnchorView(R.id.mode_switch)
                .show()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
            || super.onSupportNavigateUp()
    }
}