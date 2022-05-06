package x.stocks.valueinvesting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import x.stocks.valueinvesting.DatabaseHelper.DatabaseHelper;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.navigation.NavigationView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    Boolean buySellWasDownloaded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if(savedInstanceState==null){
        //without clicking just when it starts opens this fragment

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new MapFragment()).commit();
        navigationView.setCheckedItem(R.id.sale);}

        drawer.openDrawer(GravityCompat.START);

        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
                buySellWasDownloaded = BuySellHasRows();
                EnabledOrDisableMenuItemOnNavigationDrawer();
            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuMap:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new MapFragment()).addToBackStack(null).commit();
                return true;
            case R.id.menuExit:
                this.finishAffinity();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed(){
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.customers:

                CustomersFragment customersFragment = new CustomersFragment();

                //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                  //      customersFragment).addToBackStack(null).commit();

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        customersFragment).commit();

                Bundle bundleC = new Bundle();
                bundleC.putString("supplier", String.valueOf("0"));
                bundleC.putString("insert", "1");
                bundleC.putString("customerId", "");
                bundleC.putString("customerName", "");
                bundleC.putString("customerEmail", "");
                bundleC.putString("customerTelephone", "");
                bundleC.putString("customerObservations", "");
                bundleC.putString("customerAddress", "");
                bundleC.putString("customerCity", "");
                bundleC.putString("customerLatitud", "");
                bundleC.putString("customerLongitud", "");
                customersFragment.setArguments(bundleC);

                break;
            case R.id.viewCustomers:

                ViewSuppliersFragment viewCustomers = new ViewSuppliersFragment();

                Bundle bundleVC = new Bundle();
                bundleVC.putString("SupplierOCustomer", "0");
                bundleVC.putString("title", "VIEW CUSTOMERS");
                viewCustomers.setArguments(bundleVC);

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        viewCustomers).commit();
                break;
            case R.id.suppliers:
                CustomersFragment suppliersFragment = new CustomersFragment();

                Bundle bundleS = new Bundle();
                bundleS.putString("supplier", String.valueOf("1"));
                bundleS.putString("insert", "1");
                bundleS.putString("customerId", "");
                bundleS.putString("customerName", "");
                bundleS.putString("customerEmail", "");
                bundleS.putString("customerTelephone", "");
                bundleS.putString("customerObservations", "");
                bundleS.putString("customerAddress", "");
                bundleS.putString("customerCity", "");
                bundleS.putString("customerLatitud", "");
                bundleS.putString("customerLongitud", "");
                suppliersFragment.setArguments(bundleS);

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        suppliersFragment).commit();
                break;

            case R.id.viewSuppliers:
                ViewSuppliersFragment viewSuppliers = new ViewSuppliersFragment();
                Bundle bundleVS = new Bundle();
                bundleVS.putString("SupplierOCustomer", "1");
                bundleVS.putString("title", "VIEW SUPPLIERS");
                viewSuppliers.setArguments(bundleVS);

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        viewSuppliers).commit();
                break;
            case R.id.items:
                ItemsFragment itemsFragment = new ItemsFragment();
                Bundle bundleItems = new Bundle();
                bundleItems.putString("insert", "1");
                bundleItems.putString("itemId", "");
                bundleItems.putString("itemName", "");
                itemsFragment.setArguments(bundleItems);

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        itemsFragment).commit();
                break;
            case R.id.viewItems:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ViewItemsFragment()).commit();
                break;
            case R.id.sale:
                SaleFragment saleFragment = new SaleFragment();

                Bundle bundleSale = new Bundle();
                bundleSale.putString("insert", "1");
                bundleSale.putString("saleId", "");
                bundleSale.putString("saleCustomerName", "");
                bundleSale.putString("saleItemName", "");
                bundleSale.putString("saleObservations", "");
                bundleSale.putString("saleQty", "");
                bundleSale.putString("salePrice", "");
                saleFragment.setArguments(bundleSale);

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        saleFragment).commit();
                break;
            case R.id.purchase:
                PurchaseFragment purchaseFragment = new PurchaseFragment();

                Bundle bundlePurchase = new Bundle();
                bundlePurchase.putString("insert", "1");
                bundlePurchase.putString("purchaseId", "");
                bundlePurchase.putString("purchaseSupplierName", "");
                bundlePurchase.putString("purchaseItemName", "");
                bundlePurchase.putString("purchaseObservations", "");
                bundlePurchase.putString("purchaseQty", "");
                bundlePurchase.putString("purchasePrice", "");
                purchaseFragment.setArguments(bundlePurchase);

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        purchaseFragment).commit();
                break;
            case R.id.Sync:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new SyncFragment()).commit();
                break;
            case R.id.ViewPurchase:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ViewPurchasesFragment()).commit();
                break;
            case R.id.ViewSales:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ViewSalesFragment()).commit();
                break;
            case R.id.Graphs:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new GraphsFragment()).commit();
                break;
                }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private Boolean BuySellHasRows() {

        Boolean hasRows = false;

        try {
            // database handler
            DatabaseHelper db = new DatabaseHelper(this);
            List<String> lables = db.getAllClasses("Categoria", "");

            for(int i = 0; i< lables.size(); i++){
                hasRows = true;
                break;
            }

            return hasRows;

        } catch (Exception e) {
            hasRows = false;
            return hasRows;
        }
    }

    private void EnabledOrDisableMenuItemOnNavigationDrawer(){
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Menu menu =navigationView.getMenu();
        MenuItem target = menu.findItem(R.id.Graphs);
        // target.setVisible(false);
        if(buySellWasDownloaded== false) {
            target.setEnabled(false);
        }else{
            target.setEnabled(true);
        }
    }
}