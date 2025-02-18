package com.example.ex34_customalertdi;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

/**
 * MainActivity is the main screen of the app.
 * It provides five different buttons that each trigger an AlertDialog with different options.
 *
 * This activity demonstrates various features such as displaying simple messages,
 * handling single or multiple selections, and dynamically changing the background color.
 * It also includes functionality to capture text input and display it using Toast messages.
 *
 * Additionally, it contains a menu option to navigate to a credits screen.
 *
 * @author Gali Lavi <gl7857@bs.amalnet.k12.il>
 * @version 1.0
 * @since 14/02/2025
 */
public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private AlertDialog.Builder adb;
    private LinearLayout mydialog;
    private EditText eTtype, eTfirstTerm, eTdiff_ratio;
    private ListView lVFirstTwenty;
    private TextView tvFirstTerm, difOrMul, qOrd, index, sumOfTerms;
    private double firstTerm, difference, multiplier;
    private String[] terms = new String[20];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvFirstTerm = findViewById(R.id.firstTerm);
        difOrMul = findViewById(R.id.difOrMul);
        qOrd = findViewById(R.id.qOrd);
        index = findViewById(R.id.index);
        sumOfTerms = findViewById(R.id.sumOfTerms);
        lVFirstTwenty = findViewById(R.id.listV);
    }

    public static String differentView(double term) {
        if (term % 1 == 0 && term < 10000 && term > -10000) {
            return String.valueOf((int) term);
        }
        if (term >= 10000 || term <= -10000) {
            int exponent = 0;
            double coefficient = term;

            while (Math.abs(coefficient) >= 10000) {
                coefficient /= 10;
                exponent++;
            }
            return String.format("%d * 10^%d", (int) coefficient, exponent);
        }
        int exponent = 0;
        double coefficient = term;
        if (Math.abs(term) >= 1) {
            while (Math.abs(coefficient) >= 10) {
                coefficient /= 10;
                exponent++;
            }
        } else {
            while (Math.abs(coefficient) < 1) {
                coefficient *= 10;
                exponent--;
            }
        }
        return String.format("%.3f * 10^%d", coefficient, exponent);
    }

    public boolean check(String st) {
        return st.equals("+") || st.equals("+.") || st.equals("-") || st.equals("-.") || st.equals(".") || st.isEmpty();
    }

    public void dataEnter(View view) {
        mydialog = (LinearLayout) getLayoutInflater().inflate(R.layout.my_dialog, null);
        eTtype = mydialog.findViewById(R.id.eTtype);
        eTfirstTerm = mydialog.findViewById(R.id.eTfirstTerm);
        eTdiff_ratio = mydialog.findViewById(R.id.eTdiff_ratio);

        adb = new AlertDialog.Builder(this);
        adb.setView(mydialog);
        adb.setTitle("Data Enter");
        adb.setMessage("Please enter: series's type & first term & difference or ratio");
        adb.setPositiveButton("Enter", myclick);
        adb.setNegativeButton("Cancel", myclick);
        adb.setNeutralButton("Reset", myclick);


        adb.show();
    }

    DialogInterface.OnClickListener myclick = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (which == DialogInterface.BUTTON_POSITIVE) {
                String str1 = eTtype.getText().toString();
                String str2 = eTfirstTerm.getText().toString();
                String str3 = eTdiff_ratio.getText().toString();

                if (check(str1) || (!str1.equals("0") && !str1.equals("1")) || check(str2) || check(str3)) {
                    Toast.makeText(MainActivity.this, "Invalid input. Please enter again.", Toast.LENGTH_SHORT).show();
                } else {
                    int type = Integer.parseInt(str1);
                    firstTerm = Double.parseDouble(str2);
                    if (type == 0) {
                        difference = Double.parseDouble(str3);
                        qOrd.setText("d = ");
                        difOrMul.setText(String.valueOf(difference));
                        tvFirstTerm.setText(String.valueOf(firstTerm));
                        generateArithmeticSeries();
                    } else {
                        multiplier = Double.parseDouble(str3);
                        qOrd.setText("q = ");
                        difOrMul.setText(String.valueOf(multiplier));
                        tvFirstTerm.setText(String.valueOf(firstTerm));
                        generateGeometricSeries();
                    }

                    ArrayAdapter<String> adp = new ArrayAdapter<>(MainActivity.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, terms);
                    lVFirstTwenty.setAdapter(adp);
                    lVFirstTwenty.setOnItemClickListener(MainActivity.this);
                }
            }

            if (which == DialogInterface.BUTTON_NEGATIVE) {
                dialog.cancel();
            }

            if (which == DialogInterface.BUTTON_NEUTRAL) {
                resetData();
            }

        }
    };

    public void resetData() {
        eTtype.setText("");
        eTfirstTerm.setText("");
        eTdiff_ratio.setText("");

        tvFirstTerm.setText("");
        difOrMul.setText("");
        qOrd.setText("");
        index.setText("");
        sumOfTerms.setText("");

        for (int i = 0; i < terms.length; i++) {
            terms[i] = "";
        }

        ArrayAdapter<String> adp = new ArrayAdapter<>(MainActivity.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, terms);
        lVFirstTwenty.setAdapter(adp);

        Toast.makeText(this, "All data has been reset!", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
        index.setText(String.valueOf(pos + 1));

        String sum;
        if (eTtype.getText().toString().equals("0")) {
            sum = differentView(sumArithmetic(pos + 1));
        } else {
            sum = differentView(sumGeometric(pos + 1));
        }
        sumOfTerms.setText(sum);
    }

    public void generateArithmeticSeries() {
        for (int i = 0; i < 20; i++) {
            double term = firstTerm + i * difference;
            terms[i] = differentView(term);
        }
    }

    public void generateGeometricSeries() {
        for (int i = 0; i < 20; i++) {
            double term = firstTerm * Math.pow(multiplier, i);
            terms[i] = differentView(term);
        }
    }

    public double sumArithmetic(int n) {
        return n * (2 * firstTerm + (n - 1) * difference) / 2;
    }

    public double sumGeometric(int n) {
        if (multiplier == 1) {
            return firstTerm * n;
        }
        return firstTerm * (Math.pow(multiplier, n) - 1) / (multiplier - 1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.creditsmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menuCredits) {
            Intent si = new Intent(this, credits.class);
            startActivity(si);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
