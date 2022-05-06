package x.stocks.valueinvesting.DatabaseHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Handler;
import android.util.Base64;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import androidx.annotation.Nullable;
import x.stocks.valueinvesting.Models.BuySellModel;
import x.stocks.valueinvesting.Models.CustomerModel;
import x.stocks.valueinvesting.Models.ItemSalesDataModel;
import x.stocks.valueinvesting.Models.ItemsModel;
import x.stocks.valueinvesting.Models.PriceListsModel;
import x.stocks.valueinvesting.Models.PricesModel;
import x.stocks.valueinvesting.Models.PurchasesModel;
import x.stocks.valueinvesting.Models.SalesModel;
import x.stocks.valueinvesting.Models.SalesPersonsModel;


public class DatabaseHelper extends SQLiteOpenHelper {

    //Tabla Map
    public static final String tableMap = "Map";

    public static final String columnid_Map = "id";
    public static final String columnSupplier_Map = "Supplier";
    public static final String columnContact_Map = "Contact";
    public static final String columnAddress_Map = "Address";
    public static final String columnCity_Map = "City";
    public static final String columnTelephone_Map = "Telephone";
    public static final String columnEmail_Map = "Email";
    public static final String columnComment_Map = "Comment";
    public static final String columnLatitude_Map = "Latitude";
    public static final String columnLongitude_Map = "Longitude";
    public static final String columnCreateDate_Map = "Create_Date";
    public static final String columnUpdateDate_Map = "Update_Date";
    public static final String columnCreatedInApp_Map = "CreatedInApp";
    public static final String columnSincronizado_Map = "Sincronizado";
    public static final String columnImageMap = "Image";

    //Tabla PurchasedItems
    public static final String tablePurchasedItems = "PurchasedItems";

    public static final String columnIdPurchasedItems = "Id";
    public static final String columnItemIdPurchasedItems = "ItemId";
    public static final String columnSupplierIdPurchasedItems = "SupplierId";
    public static final String columnPurchaseQtyPurchasedItems = "PurchaseQuantity";
    public static final String columnPurchasePricePurchasedItems = "PurchasePrice";
    public static final String columnPurchaseDatePurchaseItems = "PurchaseDate";
    public static final String columnPurchaseObservations = "Observations";
    public static final String columnPurchaseInApp = "PurchaseInApp";
    public static final String columnSincronizadoPurchased = "Sincronizado";

    //Tabla SoldItems
    public static final String tableSoldItems = "SoldItems";

    public static final String columnIdSoldItems = "Id";
    public static final String columnItemIdSoldItems = "ItemId";
    public static final String columnClientId = "ClientId";
    public static final String columnSoldQty = "SaleQuantity";
    public static final String columnSoldPrice = "SalePrice";
    public static final String columnSoldDate = "SaleDate";
    public static final String columnSoldObservations = "Observations";
    public static final String columnProfitSoldItems = "Profit";
    public static final String columnSoldInApp = "SoldInApp";
    public static final String columnSincronizadoSold = "Sincronizado";

    //Tabla CatMap
    public static final String tableCatMap = "CatMap";

    public static final String columnIdCatMap = "Id";
    public static final String columnCodCatMap = "Cod";
    public static final String columnCategoryCatMap = "Category";
    public static final String columnNivel = "Nivel";
    public static final String columnCdate = "CcreationDate";
    public static final String columnUdate = "CupdateDate";
    public static final String columnImageCatMap = "Image";
    public static final String columnCatMapInApp = "CreatedInApp";
    public static final String columnAddedImageInApp = "ImagenAnadidaEnApp";
    public static final String columnSincronizadoItems = "Sincronizado";

    //Tabla PriceLists
    public static final String tablePriceLists = "PriceLists";

    public static final String columnIdPriceLists = "Id";
    public static final String columnCategoryPriceLists = "Category";
    public static final String columnTipoPriceLists = "Tipo";

    //Tabla SalesPersons
    public static final String tableSalesPersons = "SalesPersons";

    public static final String columnIdSalesPersons = "Id";
    public static final String columnSalesPersonName = "SalesPersonName";

    //Tabla Prices
    public static final String tablePrices = "Prices";

    public static final String columnIdPrices = "Id";
    public static final String columnPriceListIdPrices = "PriceListId";
    public static final String columnItemIdPrices = "ItemId";
    public static final String columnPrice = "Price";

    //Tabla BuySell
    public static final String tableBuySell = "BuySell";

    public static final String columnIdBuySell = "Id";
    public static final String columnItemIdBuySell = "ItemId";
    public static final String columnCodBuySell = "Cod";
    public static final String columnPurchaseDateBuySell = "PurchaseDate";
    public static final String columnSaleDateBuySell = "SaleDate";
    public static final String columnPurchasePriceBuySell = "PurchasePrice";
    public static final String columnPurchaseQuantityBuySell = "PurchaseQuantity";
    public static final String columnSalePriceBuySell = "SalePrice";
    public static final String columnSaleQuantityBuySell = "SaleQuantity";
    public static final String columnAvailableQuantityBuySell = "AvailableQuantity";
    public static final String columnClaseBuySell = "Clase";
    public static final String columnCategoriaBuySell = "Categoria";
    public static final String columnSubcategoriaBuySell = "Subcategoria";
    public static final String columnTipoBuySell = "Tipo";
    public static final String columnSubTipoBuySell = "SubTipo";
    public static final String columnProfitBuySell = "Profit";

    public DatabaseHelper(@Nullable Context context) {
        super(context, "Stocks.db", null, 1);
    }

    Handler h = new Handler();

    @Override
    public void onCreate(SQLiteDatabase db) {

        //Creo la tabla BuySell
        String createTableBuySellStatement = "CREATE TABLE " + tableBuySell + "(" + columnIdBuySell + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + columnItemIdBuySell + " TEXT," + columnCodBuySell + " TEXT, " + columnPurchaseDateBuySell + " TEXT, "
                + columnSaleDateBuySell + " TEXT," + columnPurchasePriceBuySell + " DOUBLE, " + columnPurchaseQuantityBuySell + " INTEGER, " + columnSalePriceBuySell +
                " DOUBLE, " + columnSaleQuantityBuySell + " INTEGER, " + columnAvailableQuantityBuySell + " INTEGER, " + columnClaseBuySell + " TEXT, " +
                columnCategoriaBuySell + " TEXT, " + columnSubcategoriaBuySell + " TEXT, " + columnTipoBuySell + " TEXT, " + columnSubTipoBuySell + " TEXT, " + columnProfitBuySell + " DOUBLE)";
        db.execSQL(createTableBuySellStatement);

        //Creo la tabla de clientes y Proveedores que se llama Map
        String createTableMapStatement = "CREATE TABLE " + tableMap + "(ID TEXT, "
                + columnContact_Map + " TEXT," + columnSupplier_Map + " BOOLEAN, " + columnAddress_Map + " TEXT, "
                + columnCity_Map + " TEXT," + columnTelephone_Map + " INTEGER, " + columnEmail_Map + " TEXT, " + columnComment_Map +
                " TEXT, " + columnLatitude_Map + " DOUBLE, " + columnLongitude_Map + " DOUBLE, " + columnCreateDate_Map + " TEXT, " +
                columnUpdateDate_Map + " TEXT, " + columnCreatedInApp_Map + " BOOLEAN, " + columnSincronizado_Map + " BOOLEAN, " +
                columnImageMap + " BLOB)";
        db.execSQL(createTableMapStatement);

        //Creo la tabla SoldItems que contiene los items
        String createTableSoldItemsStatement = "CREATE TABLE " + tableSoldItems + "(" + columnIdSoldItems + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + columnItemIdSoldItems + " INTEGER, " + columnClientId + " INTEGER, " + columnSoldQty + " DOUBLE, " + columnSoldPrice +
                " DOUBLE, " + columnSoldDate + " DOUBLE, " + columnSoldObservations + " TEXT, " + columnProfitSoldItems + " DOUBLE, "
                + columnSoldInApp + " BOOLEAN, " + columnSincronizadoSold + " BOOLEAN)";
        db.execSQL(createTableSoldItemsStatement);

        //Creo la tabla PurchasedItems que contiene los items
        String createTablePurchasedItemsStatement = "CREATE TABLE " + tablePurchasedItems + "(" + columnIdPurchasedItems + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + columnItemIdPurchasedItems + " INTEGER, " + columnSupplierIdPurchasedItems + " INTEGER, " + columnPurchaseQtyPurchasedItems + " DOUBLE, " + columnPurchasePricePurchasedItems +
                " DOUBLE, " + columnPurchaseDatePurchaseItems + " TEXT, " + columnPurchaseObservations + " TEXT, "
                + columnPurchaseInApp + " BOOLEAN, " + columnSincronizadoPurchased + " BOOLEAN)";
        db.execSQL(createTablePurchasedItemsStatement);

        //Creo la tabla CatMap que contiene los items
        String createTableCatMapStatement = "CREATE TABLE " + tableCatMap + "(" + columnIdCatMap + " TEXT, " + columnCodCatMap +
                " TEXT, " + columnCategoryCatMap + " TEXT, " + columnNivel + " INTEGER, " + columnCdate + " TEXT, " + columnUdate + " TEXT, " +
                columnImageCatMap + " BLOB, " + columnCatMapInApp + " BOOLEAN, " + columnAddedImageInApp + " BOOLEAN, " + columnSincronizadoItems + " BOOLEAN)";
        db.execSQL(createTableCatMapStatement);

        //Creo la tabla PriceLists
        String createTablePriceListsStatement = "CREATE TABLE " + tablePriceLists + "(" + columnIdPriceLists + " INTEGER, " + columnCategoryPriceLists +
                " TEXT, " + columnTipoPriceLists + " TEXT)";
        db.execSQL(createTablePriceListsStatement);

        //Creo la tabla SalesPersons
        String createTableSalesPersonsStatement = "CREATE TABLE " + tableSalesPersons + "(" + columnIdSalesPersons + " INTEGER, " + columnSalesPersonName +
                " TEXT)";
        db.execSQL(createTableSalesPersonsStatement);

        //Creo la tabla Prices
        String createTablePricesStatement = "CREATE TABLE " + tablePrices + "(" + columnIdPrices + " INTEGER, " + columnPriceListIdPrices +
                " INTEGER, " + columnItemIdPrices + " INTEGER, " + columnPrice + " DOUBLE)";
        db.execSQL(createTablePricesStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean addOneBuySell(BuySellModel buysellModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
      //  cv.put(columnIdBuySell, buysellModel.getId());
        cv.put(columnItemIdBuySell, buysellModel.getItemId());
        cv.put(columnCodBuySell, buysellModel.getCod());
        cv.put(columnPurchaseDateBuySell, buysellModel.getPurchaseDate());
        cv.put(columnSaleDateBuySell, buysellModel.getSaleDate());
        cv.put(columnPurchasePriceBuySell, buysellModel.getPurchasePrice());
        cv.put(columnPurchaseQuantityBuySell, buysellModel.getPurchaseQuantity());
        cv.put(columnSalePriceBuySell, buysellModel.getSalePrice());
        cv.put(columnSaleQuantityBuySell, buysellModel.getSaleQuantity());
        cv.put(columnAvailableQuantityBuySell, buysellModel.getAvailableQuantity());
        cv.put(columnClaseBuySell, buysellModel.getClase());
        cv.put(columnCategoriaBuySell, buysellModel.getCategoria());
        cv.put(columnSubcategoriaBuySell, buysellModel.getSubcategoria());
        cv.put(columnTipoBuySell, buysellModel.getTipo());
        cv.put(columnSubTipoBuySell, buysellModel.getSubTipo());
        cv.put(columnProfitBuySell, buysellModel.getProfit());

        long insert = db.insert(tableBuySell, null, cv);
        if (insert == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean addOneSaleDownloaded(SalesModel salesModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(columnItemIdSoldItems, salesModel.getItemId());
        cv.put(columnClientId, salesModel.getClientId());
        cv.put(columnSoldQty, salesModel.getSaleQuantity());
        cv.put(columnSoldPrice, salesModel.getSalePrice());
        cv.put(columnSoldDate, salesModel.getSaleDate());
        cv.put(columnProfitSoldItems, salesModel.getProfit());
        cv.put(columnSoldInApp, salesModel.getSoldInApp());
        cv.put(columnSincronizadoSold, salesModel.getSincronizado());

        long insert = db.insert(tableSoldItems, null, cv);
        if (insert == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean addSalesPersons(SalesPersonsModel salesPersonsModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(columnIdSalesPersons, salesPersonsModel.getId());
        cv.put(columnSalesPersonName, salesPersonsModel.getName());

        long insert = db.insert(tableSalesPersons, null, cv);
        if (insert == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean addOneClient(CustomerModel customerModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(columnid_Map, customerModel.getId());
        cv.put(columnContact_Map, customerModel.getContacto());
        cv.put(columnSupplier_Map, customerModel.isSupplier());
        cv.put(columnAddress_Map, customerModel.getAddress());
        cv.put(columnCity_Map, customerModel.getCity());
        cv.put(columnTelephone_Map, customerModel.getTelefono());
        cv.put(columnEmail_Map, customerModel.getEmail());
        cv.put(columnComment_Map, customerModel.getComment());
        cv.put(columnLatitude_Map, customerModel.getLatitude());
        cv.put(columnLongitude_Map, customerModel.getLongitude());
        cv.put(columnCreateDate_Map, customerModel.getCreationDate());
        cv.put(columnUpdateDate_Map, customerModel.getUpdateDate());
        cv.put(columnCreatedInApp_Map, customerModel.isCreatedInApp());
        cv.put(columnSincronizado_Map, customerModel.isSincronizado());
        cv.put(columnImageMap, customerModel.getImage());

        long insert = db.insert(tableMap, null, cv);
        if (insert == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean updateOneClient(CustomerModel customerModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(columnContact_Map, customerModel.getContacto());
        cv.put(columnTelephone_Map, customerModel.getTelefono());
        cv.put(columnEmail_Map, customerModel.getEmail());
        cv.put(columnComment_Map, customerModel.getComment());
        cv.put(columnUpdateDate_Map, customerModel.getUpdateDate());
        cv.put(columnImageMap, customerModel.getImage());

        long insert = db.update(tableMap, cv, columnid_Map + " = " + customerModel.getId(), null);
        if (insert == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean addOneItem(ItemsModel itemModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(columnIdCatMap, itemModel.getId());
        cv.put(columnCodCatMap, itemModel.getCod());
        cv.put(columnCategoryCatMap, itemModel.getCategory());
        cv.put(columnNivel, itemModel.getNivel());
        cv.put(columnCdate, itemModel.getCreationDate());
        cv.put(columnUdate, itemModel.getUpdateDate());
        cv.put(columnImageCatMap, itemModel.getImage());
        cv.put(columnCatMapInApp, itemModel.getCatMapInApp());
        cv.put(columnAddedImageInApp, itemModel.getImagenAnadidaEnApp());
        cv.put(columnSincronizadoItems, itemModel.getSincronizado());

        long insert = db.insert(tableCatMap, null, cv);
        if (insert == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean updateOneItem(ItemsModel itemModel, String itemId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(columnCategoryCatMap, itemModel.getCategory());
        cv.put(columnUdate, itemModel.getUpdateDate());
        cv.put(columnImageCatMap, itemModel.getImage());
        cv.put(columnAddedImageInApp, itemModel.getImagenAnadidaEnApp());

        long insert = db.update(tableCatMap, cv, columnIdCatMap + " = " + itemId, null);
        if (insert == -1) {
            return false;
        } else {
            return true;
        }
    }
    public boolean addOnePriceList(PriceListsModel priceListsModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(columnIdPriceLists, priceListsModel.getId());
        cv.put(columnCategoryPriceLists, priceListsModel.getCategory());
        cv.put(columnTipoPriceLists, priceListsModel.getTipo());

        long insert = db.insert(tablePriceLists, null, cv);
        if (insert == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean addOnePrice(PricesModel pricesModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(columnIdPrices, pricesModel.getId());
        cv.put(columnPriceListIdPrices, pricesModel.getListId());
        cv.put(columnItemIdPrices, pricesModel.getItemId());
        cv.put(columnPrice, pricesModel.getPrice());

        long insert = db.insert(tablePrices, null, cv);
        if (insert == -1) {
            return false;
        } else {
            return true;
        }
    }
    public boolean addOnePurchase(PurchasesModel purchasesModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(columnItemIdPurchasedItems, purchasesModel.getItemId());
        cv.put(columnSupplierIdPurchasedItems, purchasesModel.getSupplierId());
        cv.put(columnPurchaseQtyPurchasedItems, purchasesModel.getPurchaseQuantity());
        cv.put(columnPurchasePricePurchasedItems, purchasesModel.getPurchasePrice());
        cv.put(columnPurchaseDatePurchaseItems, purchasesModel.getPurchaseDate());
        cv.put(columnPurchaseObservations, purchasesModel.getObservations());
        cv.put(columnPurchaseInApp, purchasesModel.getPurchaseInApp());
        cv.put(columnSincronizadoPurchased, purchasesModel.getSincronizado());

        long insert = db.insert(tablePurchasedItems, null, cv);
        if (insert == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean updateOnePurchase(PurchasesModel purchasesModel, String que) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(columnItemIdPurchasedItems, purchasesModel.getItemId());
        cv.put(columnSupplierIdPurchasedItems, purchasesModel.getSupplierId());
        cv.put(columnPurchaseQtyPurchasedItems, purchasesModel.getPurchaseQuantity());
        cv.put(columnPurchasePricePurchasedItems, purchasesModel.getPurchasePrice());
        cv.put(columnPurchaseDatePurchaseItems, purchasesModel.getPurchaseDate());
        cv.put(columnPurchaseObservations, purchasesModel.getObservations());
        cv.put(columnPurchaseInApp, purchasesModel.getPurchaseInApp());
        cv.put(columnSincronizadoPurchased, purchasesModel.getSincronizado());

        long update = db.update(tablePurchasedItems, cv, columnIdPurchasedItems + " = " + que, null);
        if (update == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean addOneSale(SalesModel salesModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(columnItemIdSoldItems, salesModel.getItemId());
        cv.put(columnClientId, salesModel.getClientId());
        cv.put(columnSoldQty, salesModel.getSaleQuantity());
        cv.put(columnSoldPrice, salesModel.getSalePrice());
        cv.put(columnSoldDate, salesModel.getSaleDate());
        cv.put(columnSoldObservations, salesModel.getObservations());
        cv.put(columnProfitSoldItems, salesModel.getProfit());
        cv.put(columnSoldInApp, salesModel.getSoldInApp());
        cv.put(columnSincronizadoSold, salesModel.getSincronizado());

        long insert = db.insert(tableSoldItems, null, cv);
        if (insert == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean updateOneSale(SalesModel salesModel, String que) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(columnItemIdSoldItems, salesModel.getItemId());
        cv.put(columnClientId, salesModel.getClientId());
        cv.put(columnSoldQty, salesModel.getSaleQuantity());
        cv.put(columnSoldPrice, salesModel.getSalePrice());
        cv.put(columnSoldDate, salesModel.getSaleDate());
        cv.put(columnSoldObservations, salesModel.getObservations());
        cv.put(columnProfitSoldItems, salesModel.getProfit());
        cv.put(columnSoldInApp, salesModel.getSoldInApp());
        cv.put(columnSincronizadoSold, salesModel.getSincronizado());

        long update = db.update(tableSoldItems, cv, columnIdSoldItems + " = " + que, null);
        if (update == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Boolean CheckIfItemAlreadyExists(String itemName) {

        Boolean existe = false;

        String selectQuery = "SELECT  * FROM " + tableCatMap + " WHERE " + columnAddedImageInApp + " = 1 AND " + columnCategoryCatMap + " = " + itemName;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                existe = true;
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        return existe;
    }

    public Integer getSalesPersonId(String name) {

        String selectQuery = "SELECT * FROM " + tableSalesPersons + " WHERE " + columnSalesPersonName + " = '" + name + "'";

        Integer id = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(0);
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        return id;

    }

    public Double getPrice(String priceListId, String itemId) {

        String selectQuery = "SELECT  * FROM " + tablePrices + " WHERE " + columnPriceListIdPrices + " = " + priceListId + " AND " + columnItemIdPrices + " = " + itemId;

        Double price= 0.0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                price = cursor.getDouble(3);
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

    return price;

    }

       public List<String> getRecordedPurchases(Integer que) {
           List<String> idList = new ArrayList<String>();
           List<String> supplierList = new ArrayList<String>();
           List<String> itemNameList = new ArrayList<String>();
           List<String> quantityList = new ArrayList<String>();
           List<String> priceList = new ArrayList<String>();

        // Select All Query from CatMap
      //  String selectQuery = "SELECT  * FROM " + tablePurchasedItems + " ORDER BY " + columnIdPurchasedItems;
           String selectQuery = "SELECT CatMap.id, CatMap.Category, PurchasedItems.PurchaseQuantity, PurchasedItems.PurchasePrice, Map.Contact, PurchasedItems.Id, PurchasedItems.Observations" +
                   " FROM  CatMap INNER JOIN" +
                   "  PurchasedItems ON CatMap.id = PurchasedItems.ItemId INNER JOIN" +
                   "  Map ON PurchasedItems.SupplierId = Map.id";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                if(que == 0){
                    idList.add(cursor.getString(0));
                }
                if(que == 1){
                    supplierList.add(cursor.getString(4));
                }else if(que == 2){
                    itemNameList.add(cursor.getString(1));
                }else if(que == 3){
                    quantityList.add(cursor.getString(2));
                } else if(que == 4){
                    priceList.add(cursor.getString(3));
                } else if(que == 5){
                    priceList.add(cursor.getString(5));
                }else{
                    priceList.add(cursor.getString(6));
                }
            } while (cursor.moveToNext());
        }

        // closing connection
   cursor.close();
        db.close();
    if(que == 0) {
        return idList;
    } else if(que == 1){
        return supplierList;
   }else if(que == 2){
        return itemNameList;
   }else if(que == 3){
        return quantityList;
   } else{
        return priceList;
   }

}

    public List<String> getRecordedItems(Integer que) {
        List<String> idList = new ArrayList<String>();
        List<String> itemNameList = new ArrayList<String>();

        // Select All Query from CatMap
        //  String selectQuery = "SELECT  * FROM " + tablePurchasedItems + " ORDER BY " + columnIdPurchasedItems;
        String selectQuery = "SELECT Id, Category FROM CatMap WHERE CreatedInApp = 1 AND Sincronizado = 0";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                if(que == 0){
                    idList.add(cursor.getString(0));
                }
                if(que == 1){
                    itemNameList.add(cursor.getString(1));
                }
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();
        if(que == 0) {
            return idList;
        } else{
            return itemNameList;
        }
    }

    public List<String> getRecordedSuppliers(Integer que, String supplierOrCustomer) {
        List<String> idList = new ArrayList<String>();
        List<String> supplierList = new ArrayList<String>();
        List<String> addressList = new ArrayList<String>();
        List<String> telephoneList = new ArrayList<String>();
        List<String> emailList = new ArrayList<String>();
        List<String> cityList = new ArrayList<String>();

        // Select All Query from CatMap
        //  String selectQuery = "SELECT  * FROM " + tablePurchasedItems + " ORDER BY " + columnIdPurchasedItems;
        String selectQuery = "SELECT ID, Contact, Address, telephone, Email, City FROM Map WHERE Image IS NOT NULL AND Supplier = " + supplierOrCustomer;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                if(que == 0){
                    idList.add(cursor.getString(0));
                }
                if(que == 1){
                    supplierList.add(cursor.getString(1));
                }else if(que == 2){
                    addressList.add(cursor.getString(2));
                }else if(que == 3){
                    telephoneList.add(cursor.getString(3));
                } else if(que == 4){
                    emailList.add(cursor.getString(4));
                }else{
                    cityList.add(cursor.getString(5));
                }
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();
        if(que == 0) {
            return idList;
        } else if(que == 1){
            return supplierList;
        }else if(que == 2){
            return addressList;
        }else if(que == 3){
            return telephoneList;
        }else if(que == 4){
            return emailList;
        } else{
            return cityList;
        }

    }

    public Double getCustomersCoordinates(Integer que, String id) {
        Double lat = 0.0;
        Double lon = 0.0;

        // Select All Query from CatMap
        //  String selectQuery = "SELECT  * FROM " + tablePurchasedItems + " ORDER BY " + columnIdPurchasedItems;
        String selectQuery = "SELECT ID, Latitude, Longitude FROM Map WHERE ID = " + id;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                if(que == 0){
                    lat = cursor.getDouble(1);
                }if(que == 1) {
                    lon = cursor.getDouble(2);
                }
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();
        if(que == 0){
            return lat;
        } else{
            return lon;
        }
    }

    public Integer getCustomersOrSupplier(String customerId) {

        int supplier = 0;
        String selectQuery = "SELECT Supplier FROM Map WHERE ID = " + customerId;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                supplier = cursor.getInt(0);
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();
        return supplier;
    }

    public String getCustomersNameForMap(String id) {
        String clientname = "";

        // Select All Query from CatMap
        //  String selectQuery = "SELECT  * FROM " + tablePurchasedItems + " ORDER BY " + columnIdPurchasedItems;
        String selectQuery = "SELECT ID, Contact, Address, telephone, Email, City FROM Map WHERE ID = " + id;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                clientname = cursor.getString(1);

            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();
        return clientname;
    }

    public List<String> getRecordedSales(Integer que) {
        List<String> idList = new ArrayList<String>();
        List<String> supplierList = new ArrayList<String>();
        List<String> itemNameList = new ArrayList<String>();
        List<String> quantityList = new ArrayList<String>();
        List<String> priceList = new ArrayList<String>();

        // Select All Query from CatMap
        //  String selectQuery = "SELECT  * FROM " + tablePurchasedItems + " ORDER BY " + columnIdPurchasedItems;
        String selectQuery = "SELECT CatMap.id, CatMap.Category, SoldItems.SaleQuantity, SoldItems.SalePrice, Map.Contact, SoldItems.Id, SoldItems.Observations" +
                " FROM  CatMap INNER JOIN" +
                "  SoldItems ON CatMap.id = SoldItems.ItemId INNER JOIN" +
                "  Map ON SoldItems.ClientId = Map.id WHERE SoldItems.SoldInApp = 1";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                if(que == 0){
                    idList.add(cursor.getString(0));
                }
                if(que == 1){
                    supplierList.add(cursor.getString(4));
                }else if(que == 2){
                    itemNameList.add(cursor.getString(1));
                }else if(que == 3){
                    quantityList.add(cursor.getString(2));
                } else if(que == 4){
                    priceList.add(cursor.getString(3));
                } else if(que == 5){
                    priceList.add(cursor.getString(5));
                }else{
                    priceList.add(cursor.getString(6));
                }
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();
        if(que == 0) {
            return idList;
        } else if(que == 1){
            return supplierList;
        }else if(que == 2){
            return itemNameList;
        }else if(que == 3){
            return quantityList;
        } else{
            return priceList;
        }

    }

    public List<String> getRecordedSalesDownloaded(String saleDate, int que) {
        List<String> returnList = new ArrayList<String>();
        returnList.clear();
        // Select All Query from CatMap
        //  String selectQuery = "SELECT  * FROM " + tablePurchasedItems + " ORDER BY " + columnIdPurchasedItems;
        String selectQuery = "SELECT ItemId, SaleDate FROM BuySell WHERE SaleDate >= '" + saleDate + "' AND SaleDate != 'null'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                if(que==1){
                    returnList.add(cursor.getString(0));
                }else{
                    returnList.add(cursor.getString(1));
                }

            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();
        return returnList;
    }

    public List<String> getAllItems(int que) {

        List<String> labels = new ArrayList<String>();
        List<String> labelIds = new ArrayList<String>();

        labels.clear();
        labelIds.clear();

        // Select All Query from CatMap
        String selectQuery = "SELECT  * FROM " + tableCatMap + " ORDER BY " + columnCategoryCatMap;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getString(2));
                labelIds.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        if (que == 1) {
            // returning lables
            return labels;
        } else {
            return labelIds;
        }

    }

    public List<String> getAllSalesPersons() {

        List<String> labels = new ArrayList<String>();

        labels.clear();

        // Select All Query from CatMap
        String selectQuery = "SELECT  * FROM " + tableSalesPersons + " ORDER BY " + columnSalesPersonName;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        return labels;
    }

    public List<String> getAllClasses(String campo, String filtro) {

        List<String> labels = new ArrayList<String>();

        labels.clear();

        // Select All Query from CatMap
        String selectQuery = "SELECT  DISTINCT(" + campo + ") FROM " + tableBuySell + " " + filtro;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();
        return labels;
    }

    public ArrayList<ItemSalesDataModel> LoadDataSales(int que, int cuantos, int topBottom, String pickAdate, int queGrafico) {
        String cuales;
        if(topBottom==0){
        cuales = "DESC";
        }else{
            cuales = "ASC";
        }

        ArrayList<ItemSalesDataModel> itemSalesDataModel = new ArrayList<>();
        String selectQuery = "";
        switch (queGrafico){
            case 1:
                if(que == 0){
                    selectQuery = "SELECT SaleQuantity, Cod FROM " + tableBuySell + " WHERE " + columnSaleDateBuySell +  " > '" + pickAdate +"' ORDER BY " + columnSaleQuantityBuySell + " " + cuales + " limit " +  cuantos;
                }else{
                    selectQuery = "SELECT Profit, Cod FROM " + tableBuySell + " WHERE " + columnSaleDateBuySell +  " > '" + pickAdate + "' ORDER BY " + columnProfitBuySell + " " + cuales + " limit " + cuantos;
                }
                break;
            case 2:
                selectQuery = "SELECT AvailableQuantity, Cod FROM " + tableBuySell + " WHERE " + columnSaleDateBuySell +  " > '" + pickAdate +"' ORDER BY " + columnAvailableQuantityBuySell + " " + cuales + " limit " +  cuantos;
            break;
        }

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        String cod;
        int value;

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                cod = cursor.getString(1);
                value = cursor.getInt(0);
                itemSalesDataModel.add(new ItemSalesDataModel(cod, value));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        return itemSalesDataModel;
    }

    public ArrayList<ItemSalesDataModel> LoadDataSalesAll(int que, int cuantos, int topBottom, String pickAdate, int queGrafico) {
        String cuales;
        if(topBottom==0){
            cuales = "DESC";
        }else{
            cuales = "ASC";
        }

        ArrayList<ItemSalesDataModel> itemSalesDataModel = new ArrayList<>();
        String selectQuery = "";
        switch (queGrafico){
            case 1:
                if(que == 0){
                    selectQuery = "SELECT SoldItems.SaleQuantity, BuySell.Cod FROM SoldItems INNER JOIN BuySell ON SoldItems.itemid = BuySell.ItemId  ORDER BY SoldItems.SaleQuantity " + cuales + " limit " +  cuantos;
                }else{
                    selectQuery = "SELECT SoldItems.Profit, BuySell.Cod FROM SoldItems INNER JOIN BuySell ON SoldItems.itemid = BuySell.ItemId ORDER BY SoldItems.Profit " + cuales + " limit " + cuantos;
                }
                break;
            case 2:
                selectQuery = "SELECT BuySell.AvailableQuantity, BuySell.Cod FROM SoldItems INNER JOIN BuySell ON SoldItems.itemid = BuySell.ItemId ORDER BY BuySell.AvailableQuantity " + cuales + " limit " +  cuantos;
                break;
        }

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        String cod;
        int value;

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                cod = cursor.getString(1);
                value = cursor.getInt(0);
                itemSalesDataModel.add(new ItemSalesDataModel(cod, value));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        return itemSalesDataModel;
    }

    public ArrayList<ItemSalesDataModel> LoadDataSalesClasses(int que, int cuantos, int topBottom, String pickAdate, int queGrafico, String filtro) {
        String cuales;
        if(topBottom==0){
            cuales = "DESC";
        }else{
            cuales = "ASC";
        }

        ArrayList<ItemSalesDataModel> itemSalesDataModel = new ArrayList<>();
        String selectQuery = "";
        switch (queGrafico){
            case 1:
                if(que == 0){
                    selectQuery = "SELECT SUM(SaleQuantity) AS SaleQuantity, Clase FROM " + tableBuySell + " WHERE " + columnSaleDateBuySell +  " > '" + pickAdate +"' GROUP BY Clase ORDER BY " + columnSaleQuantityBuySell + " " + cuales + " limit " +  cuantos;
                }else{
                    selectQuery = "SELECT SUM(Profit) AS Profit, Clase FROM " + tableBuySell + " WHERE " + columnSaleDateBuySell +  " > '" + pickAdate + "' GROUP BY Clase ORDER BY " + columnProfitBuySell + " " + cuales + " limit " + cuantos;
                }
                break;
            case 2:
                if(que == 0){
                    selectQuery = "SELECT SUM(SaleQuantity) AS SaleQuantity, Categoria FROM " + tableBuySell + " WHERE " + columnSaleDateBuySell +  " > '" + pickAdate +"' AND Clase = '" + filtro + "' GROUP BY Categoria ORDER BY " + columnSaleQuantityBuySell + " " + cuales + " limit " +  cuantos;
                }else{
                    selectQuery = "SELECT SUM(Profit) AS Profit, Categoria FROM " + tableBuySell + " WHERE " + columnSaleDateBuySell +  " > '" + pickAdate + "' AND Clase = '" + filtro + "' GROUP BY Categoria ORDER BY " + columnProfitBuySell + " " + cuales + " limit " + cuantos;
                }
                break;
            case 3:
                if(que == 0){
                    selectQuery = "SELECT SUM(SaleQuantity) AS SaleQuantity, Subcategoria FROM " + tableBuySell + " WHERE " + columnSaleDateBuySell +  " > '" + pickAdate + "' AND Categoria = '" + filtro + "' GROUP BY Subcategoria ORDER BY " + columnSaleQuantityBuySell + " " + cuales + " limit " +  cuantos;
                }else{
                    selectQuery = "SELECT SUM(Profit) AS Profit, Subcategoria FROM " + tableBuySell + " WHERE " + columnSaleDateBuySell +  " > '" + pickAdate + "' AND Categoria = '" + filtro + "' GROUP BY Subcategoria ORDER BY " + columnProfitBuySell + " " + cuales + " limit " + cuantos;
                }
                break;
        }

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        String cod;
        int value;

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                cod = cursor.getString(1);
                value = cursor.getInt(0);
                itemSalesDataModel.add(new ItemSalesDataModel(cod, value));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        return itemSalesDataModel;
    }

    public Integer getSelectedItemId(String itemName) {
       Integer selectedItemId = 0;
        // Select All Query from CatMap
        String selectQuery = "SELECT  * FROM " + tableCatMap + " WHERE " + columnCategoryCatMap + " = '" + itemName + "'" ;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                selectedItemId = cursor.getInt(0);
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

       return selectedItemId;

    }
    public boolean UpdateImage(String i, byte[] img, Boolean imagenAnadidaEnApp) {
        SQLiteDatabase db = this.getWritableDatabase();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());
        String creationDate = sdf.format(new Date());

        ContentValues contentValues = new ContentValues();
        contentValues.put("Image", img);
        contentValues.put("CupdateDate", creationDate);
        if(imagenAnadidaEnApp == true){
            contentValues.put("ImagenAnadidaEnApp", 1);
        }else{
            contentValues.put("ImagenAnadidaEnApp", 0);
        }

        long update = db.update(tableCatMap, contentValues, columnIdCatMap + " = ?", new String[] {i});
        if (update == -1) {
            return false;
        } else {
            return true;
        }
    }

    public String getStringImage(int position){
        // Select All Query from CatMap
        String selectQuery = "SELECT  * FROM " + tablePurchasedItems + " ORDER BY " + columnIdPurchasedItems;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        byte[] imag = null;
        String encodedImage = "";
        try {
            // looping through all rows and adding to list
            cursor.moveToFirst();
            imag = cursor.getBlob(6);
            encodedImage = Base64.encodeToString(imag, Base64.NO_WRAP);
        }catch(Exception e){

        }
        // closing connection
        cursor.close();
        db.close();

        return encodedImage;
    }

    public Bitmap getImage(int position){
       // Select All Query from CatMap
        String selectQuery = "SELECT  * FROM " + tableCatMap + " WHERE Id = " + position;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        byte[] imag = null;
        Bitmap bt = null;
        try {
            // looping through all rows and adding to list
            cursor.moveToFirst();
            imag = cursor.getBlob(6);
            bt = BitmapFactory.decodeByteArray(imag, 0, imag.length);
        }catch(Exception e){

        }

        // closing connection
        cursor.close();
        db.close();

        return bt;
    }

    public byte[] getImageBytesFromCatMap(int position){
        // Select All Query from CatMap
       String selectQuery = "SELECT  * FROM " + tableCatMap + " WHERE Id = " + position;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        byte[] imag = null;
        Bitmap bt = null;
        try {
            // looping through all rows and adding to list
            cursor.moveToFirst();
            imag = cursor.getBlob(6);
        }catch(Exception e){

        }

        // closing connection
        cursor.close();
        db.close();

        return imag;
    }

    public byte[] getImageBytesFromMap(int position){
        // Select All Query from CatMap
        String selectQuery = "SELECT  * FROM " + tableMap + " WHERE id = " + position;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        byte[] imag = null;
        Bitmap bt = null;
        try {
            // looping through all rows and adding to list
            cursor.moveToFirst();
            imag = cursor.getBlob(14);
        }catch(Exception e){

        }

        // closing connection
        cursor.close();
        db.close();

        return imag;
    }

    public String getClientstoUpSync(Integer Id) {

        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        String timeNow = formatter.format(date);

        String selectQuery = "SELECT  * FROM Map WHERE CreatedInApp = 1 And Id = " + Id;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        String  jsonClientstoUpSync = "";


        try {
            // looping through all rows and adding to list
            cursor.moveToFirst();

            //serializo la imagen a base64
            byte[] img = cursor.getBlob(14);
            String imgString =  Base64.encodeToString(img, Base64.NO_WRAP);

            //formato la fecha a una fecha valida
            String strFechaCreacion = cursor.getString(10);
            String strFechaUpdate = cursor.getString(11);

            jsonClientstoUpSync =

                    "{\n" +
                            "  \"id\": " + cursor.getString(0) + ",\n" +
                            "  \"Contacto\": " + "\"" + cursor.getString(1) +  "\"" + ",\n" +
                            "  \"Supplier\": " + cursor.getString(2) + ",\n" +
                            "  \"Address\": " + "\"" + cursor.getString(3) +  "\"" + ",\n" +
                            "  \"City\": " + "\"" + cursor.getString(4) +  "\"" + ",\n" +
                            "  \"telefono\": " + cursor.getString(5) + ",\n" +
                            "  \"email\": " + "\"" + cursor.getString(6) +  "\"" + ",\n" +
                            "  \"Valor\": " + 0 + ",\n" +
                            "  \"Latitud\": " + cursor.getString(8) + ",\n" +
                            "  \"Longitud\": " + cursor.getString(9) + ",\n" +
                            "  \"Comment\": " + "\"" + cursor.getString(7) +  "\"" + ",\n" +
                            "  \"FechaCreacion\": " + "\"" + strFechaCreacion +  "\"" + ",\n" +
                            "  \"FechaEdicion\": " + "\"" + strFechaUpdate +  "\"" + ",\n" +
                            "  \"CreatedInApp\": " + 1 +  ",\n" +
                            "  \"Sincronizado\": " + 0 + ",\n" +
                            "  \"Image\": " + "\"" + imgString +  "\"" + ",\n" +
                            "  \"FechaSync\": " + "\"" + timeNow +  "\"" + "\n" +
                            "}";

        }catch(Exception e){
            e.printStackTrace();
        }
        // closing connection
        cursor.close();
        db.close();
        return jsonClientstoUpSync;
    }

    public String getItemstoUpSync(Integer Id){

        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        String timeNow = formatter.format(date);

        String selectQuery = "SELECT  * FROM CatMap Where Id = " + Id;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        String  jsonItemstoUpSync = "";
        try {
            // looping through all rows and adding to list
            cursor.moveToFirst();

            //serializo la imagen a base64
            byte[] img = cursor.getBlob(6);
            String imgString =  Base64.encodeToString(img, Base64.NO_WRAP);

            //formato la fecha a una fecha valida
            String strFechaCreacion = cursor.getString(4);
            String strFechaUpdate = cursor.getString(5);

                  jsonItemstoUpSync =

                    "{\n" +
                            "  \"Id\": " + cursor.getString(0) + ",\n" +
                            "  \"Cod\": " + "\"" + cursor.getString(1) +  "\"" + ",\n" +
                            "  \"Category\": " + "\"" + cursor.getString(2) +  "\"" + ",\n" +
                            "  \"Nivel\": " +  cursor.getString(3) +  ",\n" +
                            "  \"FechaCreacion\": " + "\"" + strFechaCreacion + "\"" + ",\n" +
                            "  \"FechaEdicion\": " + "\"" + strFechaUpdate +  "\"" + ",\n" +
                            "  \"Image\": " + "\"" + imgString +  "\"" + ",\n" +
                            "  \"CreatedInApp\": " + true +  ",\n" +
                            "  \"Sincronizado\": " + false + ",\n" +
                            "  \"FechaSync\": " + "\"" + timeNow +  "\"" + "\n" +
                            "}";

        }catch(Exception e){
            e.printStackTrace();
        }
        // closing connection
        cursor.close();
        db.close();
        return jsonItemstoUpSync;
    }

    public String getPurchasestoUpSync(Integer Id) {

        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        String timeNow = formatter.format(date);

        String selectQuery = "SELECT  * FROM PurchasedItems WHERE Id = " + Id;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        String  jsonPurchasestoUpSync = "";


        try {
            // looping through all rows and adding to list
            cursor.moveToFirst();

            jsonPurchasestoUpSync =

                    "{\n" +
                            "  \"id\": " + cursor.getString(0) + ",\n" +
                            "  \"ItemId\": " + cursor.getInt(1) +  ",\n" +
                            "  \"SupplierId\": " + cursor.getInt(2) + ",\n" +
                            "  \"PurchaseQuantity\": " + cursor.getDouble(3) +  ",\n" +
                            "  \"PurchasePrice\": " + cursor.getDouble(4) + ",\n" +
                            "  \"PurchaseDate\": " + "\"" + cursor.getString(5) + "\"" + ",\n" +
                            "  \"Observations\": " + "\"" + cursor.getString(6) +  "\"" + ",\n" +
                            "  \"PurchaseInApp\": " + 1 +  ",\n" +
                            "  \"Sincronizado\": " + 0 + ",\n" +
                            "  \"FechaSync\": " + "\"" + timeNow +  "\"" + "\n" +
                            "}";

        }catch(Exception e){
            e.printStackTrace();
        }
        // closing connection
        cursor.close();
        db.close();
        return jsonPurchasestoUpSync;
    }

    public String getSoldtoUpSync(Integer Id, int salesPersonId) {

        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        String timeNow = formatter.format(date);

        String selectQuery = "SELECT  * FROM SoldItems WHERE Id = " + Id;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        String  jsonSoldtoUpSync = "";

        try {
            // looping through all rows and adding to list
            cursor.moveToFirst();

            jsonSoldtoUpSync =

                    "{\n" +
                            "  \"Id\": " + cursor.getString(0) + ",\n" +
                            "  \"ItemId\": " + cursor.getInt(1) +  ",\n" +
                            "  \"ClientId\": " + cursor.getInt(2) + ",\n" +
                            "  \"SaleQuantity\": " + cursor.getDouble(3) +  ",\n" +
                            "  \"SalePrice\": " + cursor.getDouble(4) + ",\n" +
                            "  \"SaleDate\": " + "\"" + cursor.getString(5) + "\"" + ",\n" +
                            "  \"Observations\": " + "\"" + cursor.getString(6) +  "\"" + ",\n" +
                            "  \"SoldInApp\": " + 1 + "\n" +  ",\n" +
                            "  \"Sincronizado\": " + 0 + ",\n" +
                            "  \"CodVendedor\": " + salesPersonId + ",\n" +
                            "  \"FechaSync\": " + "\"" + timeNow +  "\"" + "\n" +
                            "}";

        }catch(Exception e){
            e.printStackTrace();
        }
        // closing connection
        cursor.close();
        db.close();
        return jsonSoldtoUpSync;
    }

    public String getSyncAudittoUpSync(Integer Id, String name){

        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        String timeNow = formatter.format(date);

        String  jsonAudittoUpSync = "";
        try {
            // looping through all rows and adding to list

            jsonAudittoUpSync =

                    "{\n" +
                            "  \"id\": " + Id + ",\n" +
                            "  \"Name\": " + "\"" + name +  "\"" + ",\n" +
                            "  \"SyncDate\": " + "\"" + timeNow +  "\"" + "\n" +
                            "}";

        }catch(Exception e){
            e.printStackTrace();
        }
        return jsonAudittoUpSync;
    }

    public List<String> getAllCustomers(int que){
        List<String> labels = new ArrayList<String>();
        List<String> labelIds = new ArrayList<String>();

        // Select All Query from CatMap
        String selectQuery = "SELECT  * FROM " + tableMap + " WHERE " + columnSupplier_Map + " = 0 ORDER BY " + columnContact_Map;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getString(1));
                labelIds.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        if(que == 1){
            // returning lables
            return labels;
        }else{
            return labelIds;
        }

    }

    public List<String> getAllPriceLlists(int que){
        List<String> labels = new ArrayList<String>();
        List<String> labelIds = new ArrayList<String>();

        // Select All Query from CatMap
        String selectQuery = "SELECT  * FROM " + tablePriceLists;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getString(2));
                labelIds.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        if(que == 1){
            // returning lables
            return labels;
        }else{
            return labelIds;
        }

    }

    public List<String> getAllSuppliers(int que) {
        List<String> labels = new ArrayList<String>();
        List<String> labelIds = new ArrayList<String>();
        // Select All Query from CatMap
        String selectQuery = "SELECT  * FROM " + tableMap + " WHERE " + columnSupplier_Map + " = 1 ORDER BY " + columnContact_Map;

        String clientName;
        String clientId;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                clientName = cursor.getString(1);
                clientId = cursor.getString(0);
                labels.add(clientName);
                labelIds.add(clientId);
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        if (que == 1) {
            return labels;
        } else {
            return labelIds;
        }
    }

    public List<Integer> getAllCustomersAndProvidesIdsToGetMaxId(){
        List<Integer> labelIds = new ArrayList<>();

        // Select All Query from CatMap
        String selectQuery = "SELECT  * FROM " + tableMap + " ORDER BY ID";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                labelIds.add(cursor.getInt(0));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

            return labelIds;

    }

    public List<String> getOnlyClientsAndSuppliersCreatedInApp(Integer que) {
        List<String> labelClientsCreatedinAppIds = new ArrayList<String>();
        labelClientsCreatedinAppIds.clear();
        // Select All Query from CatMap
        String selectQuery = "SELECT  * FROM " + tableMap + " WHERE CreatedInApp = 1";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                if(que==0){
                    labelClientsCreatedinAppIds.add(cursor.getString(0));
                }else{
                    labelClientsCreatedinAppIds.add(cursor.getString(1));
                }

            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

            return labelClientsCreatedinAppIds;
    }

    public List<String> getOnlyItemsConImagenes(String createdInApp, Integer que) {
        List<String> labelItemsConImagenes = new ArrayList<String>();
        labelItemsConImagenes.clear();
        // Select All Query from CatMap
        String selectQuery = "SELECT  * FROM CatMap WHERE ImagenAnadidaEnApp = 1 AND CreatedInApp = " + createdInApp;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                if (que == 0) {
                    labelItemsConImagenes.add(cursor.getString(0));
                } else{
                    labelItemsConImagenes.add(cursor.getString(2));
            }

            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

                    return labelItemsConImagenes;

    }

    public List<String> getPurchasedIds(int que, String where) {
        List<String> labelPurchasesIds = new ArrayList<String>();
        List<String> labelItemIds = new ArrayList<String>();
        List<String> labelSupplierIds = new ArrayList<String>();

        // Select All Query from CatMap
        String selectQuery = "SELECT  * FROM PurchasedItems" + where;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        String purchaseId;
        String itemId;
        String supplierId;
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                purchaseId = cursor.getString(0);
                itemId = cursor.getString(1);
                supplierId = cursor.getString(2);
                 labelPurchasesIds.add(purchaseId);
                labelItemIds.add(itemId);
                labelSupplierIds.add(supplierId);
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();
     switch(que) {
       case 0:
        return labelPurchasesIds;
       case 1:
        return labelItemIds;
       default:
        return labelSupplierIds;
       }
   }

    public List<String> getSoldIds(int que, String where) {
        List<String> labelSoldIds = new ArrayList<String>();
        List<String> labelItemIds = new ArrayList<String>();
        List<String> labelCustomerIds = new ArrayList<String>();

        // Select All Query from CatMap
        String selectQuery = "SELECT  * FROM SoldItems" + where;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        String saleId;
        String itemId;
        String customerId;

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                saleId = cursor.getString(0);
                itemId = cursor.getString(1);
                customerId = cursor.getString(2);
                labelSoldIds.add(saleId);
                labelItemIds.add(itemId);
                labelCustomerIds.add(customerId);
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();
        switch(que) {
            case 0:
                return labelSoldIds;
            case 1:
                return labelItemIds;
            default:
                return labelCustomerIds;
        }


    }

    public void delete(String tableName){
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(tableName, null, null);
        db.close();
    }

    public int deletePurchase(Integer purchaseId){
        SQLiteDatabase db = this.getWritableDatabase();
        String table = tablePurchasedItems;
        String que = " Id = " + purchaseId;
       return db.delete(table, que, null);
    }

    public int deleteSale(Integer saleId){
        SQLiteDatabase db = this.getWritableDatabase();
        String table = tableSoldItems;
        String que = " Id = " + saleId;
        return db.delete(table, que, null);
    }

    public int deleteMapRecord(Integer mapId){
        SQLiteDatabase db = this.getWritableDatabase();
        String table = tableMap;
        String que = " ID = " + mapId;
        return db.delete(table, que, null);
    }

    public int deleteCatMapRecord(Integer itemId){
        SQLiteDatabase db = this.getWritableDatabase();
        String table = tableCatMap;
        String que = " Id = " + itemId;
        return db.delete(table, que, null);
    }
}
