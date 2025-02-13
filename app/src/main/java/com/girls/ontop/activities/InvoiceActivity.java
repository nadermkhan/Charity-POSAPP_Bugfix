package com.girls.ontop.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.print.PrintManager;
import android.util.Log;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.girls.ontop.R;
import com.girls.ontop.models.Sell;
import com.girls.ontop.models.SellListResponse;
import com.girls.ontop.network.ApiClient;
import com.girls.ontop.network.ApiService;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InvoiceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invoice_layout);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        int invoiceId = extras.getInt("invoice_id", 0);
        Log.d("Intent Data", String.valueOf(invoiceId));
        fetchinvoice(String.valueOf(invoiceId));
    }

    private String getAuthTokenFromPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        String accessToken = sharedPreferences.getString("auth_token", null);
        return sharedPreferences.getString("auth_token", null);  // Retrieve the token from SharedPreferences
    }

    private void fetchinvoice(String invoice_id) {
        String accessToken = getAuthTokenFromPreferences(); // Use the actual access token


        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
        Call<SellListResponse> call = apiService.getSellById("Bearer " + accessToken,invoice_id);

        call.enqueue(new Callback<SellListResponse>() {
            @Override
            public void onResponse(Call<SellListResponse> call, Response<SellListResponse> response) {
                SellListResponse sellresponse = response.body();
                Log.d("Sell response Full", String.valueOf(sellresponse.getSells()));
                List<Sell> selldata = sellresponse.getSells();
                Log.d("Sell response", String.valueOf(selldata));
                generateInvoiceView(selldata.get(0));
            }

            @Override
            public void onFailure(Call<SellListResponse> call, Throwable t) {
                // Handle failure (e.g., no internet, server error, etc.)
//                Log.e(TAG, "Error fetching sell list: " + t.getMessage());

            }
        });
    }

    private void generateInvoiceView(Sell selldata) {
        Log.d("SELLSA DATA", String.valueOf(selldata));

        // Retrieve the necessary views
        TextView tvInvoiceTitle = findViewById(R.id.tvInvoiceTitle);
        TextView tvCustomerName = findViewById(R.id.tvCustomerName);
        TextView tvTotalAmount = findViewById(R.id.tvTotalAmount);
        RecyclerView itemRecyclerView = findViewById(R.id.itemRecyclerView);

        // Set values to the views
        tvInvoiceTitle.setText("Invoice: " + selldata.getInvoiceNo());
        tvCustomerName.setText("Customer: " + selldata.getContact());
        tvTotalAmount.setText("Total Amount: " + selldata.getFinal_total());

        // Set up the print manager and adapter
        PrintManager printManager = (PrintManager) getSystemService(PRINT_SERVICE);
        PrintDocumentAdapter printAdapter = new PrintDocumentAdapter() {
            @Override
            public void onStart() {
                // This method will start the printing process
            }

            @Override
            public void onLayout(PrintAttributes oldAttributes, PrintAttributes newAttributes, CancellationSignal cancellationSignal, LayoutResultCallback callback, Bundle bundle) {
                // Specify page count and content type
                callback.onLayoutFinished(new PrintDocumentInfo.Builder("invoice.pdf")
                        .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                        .setPageCount(1)
                        .build(), true);
            }

            @Override
            public void onWrite(PageRange[] pages, ParcelFileDescriptor destination, CancellationSignal cancellationSignal, WriteResultCallback callback) {
                // You will need to create a PDF document here
                PdfDocument pdfDocument = new PdfDocument();

                // Create a page description for the PDF
                PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(600, 800, 1).create();
                PdfDocument.Page page = pdfDocument.startPage(pageInfo);

                // Get the canvas to draw the content
                Canvas canvas = page.getCanvas();

                // Paint object to draw text on the PDF
                Paint paint = new Paint();
                paint.setColor(Color.BLACK);
                paint.setTextSize(12f);

                // Draw the content on the PDF canvas
                canvas.drawText("Invoice: " + selldata.getInvoiceNo(), 100, 100, paint);
                canvas.drawText("Customer: " + selldata.getContact(), 100, 150, paint);
                canvas.drawText("Total Amount: " + selldata.getFinal_total(), 100, 200, paint);

                // Add additional content here (like items from RecyclerView, etc.)

                // Finish the page
                pdfDocument.finishPage(page);

                // Write the PDF document to the output file descriptor
                try {
                    FileOutputStream fileOutputStream = new FileOutputStream(destination.getFileDescriptor());
                    pdfDocument.writeTo(fileOutputStream);
                    fileOutputStream.close();
                } catch (IOException e) {
                    Log.e("InvoicePrint", "Error writing PDF file", e);
                }

                // Notify the print manager that writing is complete
                callback.onWriteFinished(new PageRange[]{PageRange.ALL_PAGES});
            }
        };

        // Trigger the print
        printManager.print("Invoice Print", printAdapter, null);
    }

}