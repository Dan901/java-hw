package hr.fer.android.jmbag0036485175;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

/**
 * {@code CalculusActivity} is a main activity offering basic mathematical operations over two
 * operands.
 *
 * @author Dan
 */
public class CalculusActivity extends AppCompatActivity {

    /**
     * Code for returning result.
     */
    private static final int REQUEST_CODE = 314;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculus);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final RadioGroup rgOperations = (RadioGroup) findViewById(R.id.rgOperations);
        final EditText etFirstNumber = (EditText) findViewById(R.id.etFirstNumber);
        final EditText etSecondNumber = (EditText) findViewById(R.id.etSecondNumber);
        Button btnCalculate = (Button) findViewById(R.id.btnCalculate);

        btnCalculate.setOnClickListener(v -> {
            RadioButton rbChecked = (RadioButton) findViewById(rgOperations.getCheckedRadioButtonId());
            if (rbChecked == null) {
                Toast.makeText(this,
                        getString(R.string.check_operation_msg), Toast.LENGTH_LONG).show();
                return;
            }

            String firstNumber = etFirstNumber.getText().toString();
            String secondNumber = etSecondNumber.getText().toString();

            Intent i = new Intent(this, DisplayActivity.class);

            double result;
            try {
                result = calculateResult(firstNumber, secondNumber, rbChecked.getId());
            } catch (NumberFormatException | ArithmeticException e) {
                i.putExtra(getString(R.string.result),
                        String.format(getString(R.string.error_msg),
                                rbChecked.getText(), firstNumber, secondNumber, e.getMessage()
                        )
                );
                startActivityForResult(i, REQUEST_CODE);
                return;
            }

            i.putExtra(getString(R.string.result),
                    String.format(getString(R.string.result_msg), rbChecked.getText(), Double.toString(result))
            );
            startActivityForResult(i, REQUEST_CODE);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_calculus, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            RadioGroup rgOperations = (RadioGroup) findViewById(R.id.rgOperations);
            rgOperations.clearCheck();

            EditText etFirstNumber = (EditText) findViewById(R.id.etFirstNumber);
            etFirstNumber.setText("");

            EditText etSecondNumber = (EditText) findViewById(R.id.etSecondNumber);
            etSecondNumber.setText("");
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Calculates a basic binary operation based on chosen button and given strings
     * that should contain numbers.
     *
     * @param firstNumber  first operand
     * @param secondNumber second operands
     * @param opBtnID      button ID for choosing the operation
     * @return result of the operation on given operands
     * @throws NumberFormatException if any of the given strings contains invalid characters
     * @throws ArithmeticException   if operation cannot be calculated
     */
    private double calculateResult(String firstNumber, String secondNumber, int opBtnID) {
        double first = Double.parseDouble(firstNumber);
        double second = Double.parseDouble(secondNumber);

        double result;
        switch (opBtnID) {
            case R.id.rbAdd:
                result = first + second;
                break;
            case R.id.rbSub:
                result = first - second;
                break;
            case R.id.rbMul:
                result = first * second;
                break;
            case R.id.rbDiv:
                if (second == 0) {
                    throw new ArithmeticException("Dividing by zero");
                }
                result = first / second;
                break;
            default:
                result = 0;
                break;
        }

        return result;
    }
}
