package sk.fri.ballay10.caloriepal

import android.app.Application
import sk.fri.ballay10.caloriepal.roomdb.AppContainer
import sk.fri.ballay10.caloriepal.roomdb.AppDataContainer
import sk.fri.ballay10.caloriepal.objects.IngredietList.initializeIngredientListContext

class CaloriePalApplication : Application() {
    /**
     * AppContainer instance used by the rest of classes to obtain dependencies
     */
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
        initializeIngredientListContext(this)
    }
}