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
    /**
     * AlertDialog.Builder used to create various types of dialog boxes in the app.
     */
    private AlertDialog.Builder adb;
    /**
     * Layout for the custom dialog.
     */
    private LinearLayout mydialog;
    /**
     * EditText views to allow the user to input sequence data (first term, difference/ratio).
     */
    private EditText eTtype, eTfirstTerm, eTdiff_ratio;
    /**
     * ListView that will display the generated terms of the sequence.
     */
    private ListView lVFirstTwenty;
    /**
     * TextViews to display the current first term, difference/ratio, sequence type, index, and sum.
     */
    private TextView tvFirstTerm, difOrMul, qOrd, index, sumOfTerms;
    /**
     * Variables to store the first term, common difference (for arithmetic sequences), and common ratio (for geometric sequences).
     */
    private double firstTerm, difference, multiplier;
    /**
     * Array to hold the terms of the sequence for display in the ListView.
     */
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

    /**
     * Converts a numerical term into a readable string format, either as a plain number or in scientific notation.
     * If the value is too large or too small, it is represented in scientific notation.
     *
     * @param term The numerical value to be formatted.
     * @return A string representation of the formatted number.
     */

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

    /**
     * Checks whether the given input string is valid.
     *
     * @param st The string to be checked.
     * @return True if the input is invalid; false otherwise.
     */

    public boolean check(String st) {
        return st.equals("+") || st.equals("+.") || st.equals("-") || st.equals("-.") || st.equals(".") || st.isEmpty();
    }

    /**
     * Displays a dialog for user input, allowing entry of the sequence type, first term,
     * and either the difference or ratio.
     *
     * @param view The view that initiates the dialog.
     */

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

    /**
     * OnClickListener for handling dialog actions (enter, cancel, reset).
     */
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

    /**
     * Resets all data and clears all fields, including sequence terms, and updates the UI.
     */
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

    /**
     * Processes the selection of a term from the ListView. Updates the displayed index
     * and computes the cumulative sum up to the selected term.
     *
     * @param adapterView The parent view containing the clicked item.
     * @param view The specific item that was selected.
     * @param pos The position of the selected item in the list.
     * @param id The unique identifier of the selected item.
     */

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

    /**
     * Generates the terms for an arithmetic sequence and stores them in the terms array.
     */
    public void generateArithmeticSeries() {
        for (int i = 0; i < 20; i++) {
            double term = firstTerm + i * difference;
            terms[i] = differentView(term);
        }
    }

    /**
     * Generates the terms for a geometric sequence and stores them in the terms array.
     */
    public void generateGeometricSeries() {
        for (int i = 0; i < 20; i++) {
            double term = firstTerm * Math.pow(multiplier, i);
            terms[i] = differentView(term);
        }
    }

    /**
     * Calculates the sum of the initial n terms in a arithmetic series.
     *
     * @param n The number of terms to sum.
     * @return The total sum of the first n terms.
     */

    public double sumArithmetic(int n) {
        return n * (2 * firstTerm + (n - 1) * difference) / 2;
    }

    /**
     * Calculates the sum of the initial n terms in a geometric series.
     *
     * @param n The number of terms to sum.
     * @return The total sum of the first n terms.
     */
    public double sumGeometric(int n) {
        if (multiplier == 1) {
            return firstTerm * n;
        }
        return firstTerm * (Math.pow(multiplier, n) - 1) / (multiplier - 1);
    }

    /**
     * Inflates the menu and adds the option for credits.
     *
     * @param menu The menu to inflate.
     * @return True if the menu was successfully inflated.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.creditsmenu, menu);
        return true;
    }

    /**
     * Handles item selection from the options menu.
     *
     * @param item The selected menu item.
     * @return True if the menu item was successfully handled.
     */
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
