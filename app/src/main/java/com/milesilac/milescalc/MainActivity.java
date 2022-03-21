package com.milesilac.milescalc;


import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TextView txtViewSmall,txtViewBig;
    private boolean hasOpeningParenthesis = false;
    private int OPCount = 0;
    private int CPCount = 0;
    private String backgroundSequence = " ";
    private int lastIndex = -1;
    private int nextIndex = -1;
    private int indexInnermostOParenthesis;
    private int indexInnermostCParenthesis;
    private String isSolved;
    private int determineOps = 0;
    private int determineCurrentSolved = 0;

    private final Button[] buttons = new Button[20];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtViewSmall = findViewById(R.id.txtViewSmall);
        txtViewBig = findViewById(R.id.txtViewBig);

        buttons[0] = findViewById(R.id.btn0);
        buttons[1] = findViewById(R.id.btn1);
        buttons[2] = findViewById(R.id.btn2);
        buttons[3] = findViewById(R.id.btn3);
        buttons[4] = findViewById(R.id.btn4);
        buttons[5] = findViewById(R.id.btn5);
        buttons[6] = findViewById(R.id.btn6);
        buttons[7] = findViewById(R.id.btn7);
        buttons[8] = findViewById(R.id.btn8);
        buttons[9] = findViewById(R.id.btn9);
        buttons[10] = findViewById(R.id.btnAdd);
        buttons[11] = findViewById(R.id.btnSubt);
        buttons[12] = findViewById(R.id.btnMult);
        buttons[13] = findViewById(R.id.btnDiv);
        buttons[14] = findViewById(R.id.btnPercent);
        buttons[15] = findViewById(R.id.btnDot);
        buttons[16] = findViewById(R.id.btnParenthesis);
        buttons[17] = findViewById(R.id.btnEquals);
        buttons[18] = findViewById(R.id.btnC);
        buttons[19] = findViewById(R.id.btnQuit);

        for (int i=0;i<10;i++) {
            int currentNum = i;
            buttons[i].setOnClickListener(v -> toInputChar(String.valueOf(currentNum)));
        }

        for (int i=10;i<20;i++) {
            int currentOp = i;
            buttons[i].setOnClickListener(v -> {
                switch (currentOp) {
                    case 10:
                        getTextOp("+");
                        break;
                    case 11:
                        getTextOp("-");
                        break;
                    case 12:
                        getTextOp("×");
                        break;
                    case 13:
                        getTextOp("÷");
                        break;
                    case 14:
                        getTextOp("%");
                        break;
                    case 15:
                        getTextOp(".");
                        break;
                    case 16:
                        toCheckParenthesis();
                        break;
                    case 17:
                        toCalc();
                        break;
                    case 18:
                        toClear();
                        break;
                    case 19:
                        finish();
                        break;
                }
            }); //onClickListener
        }

        buttons[18].setOnLongClickListener(v -> {
            while (!txtViewBig.getText().toString().equals("0") && !txtViewSmall.getText().toString().equals("0")) {
                toClear();
            }
            return true;
        });

    }

    public void toInputChar(String s) {
        if (txtViewSmall.getText().toString().endsWith(")")) {
            String getTextChar = txtViewSmall.getText() + "×" + s;
            txtViewSmall.setText(getTextChar);
            backgroundSequence = backgroundSequence.trim() + "×" + s;
        }
        else {
            String getTextChar = txtViewSmall.getText() + s;
            txtViewSmall.setText(getTextChar);
            backgroundSequence = backgroundSequence.trim() + s;
        }
        System.out.println("current backgroundsequence: " + backgroundSequence);
    } //toInputChar

    public void getTextOp(String op) {
        String getTextWithOp = txtViewSmall.getText() + op;
        txtViewSmall.setText(getTextWithOp);
        if (op.equals("-")) {
            backgroundSequence = backgroundSequence.trim() + "+-";
        }
        else {
            backgroundSequence = backgroundSequence.trim() + op;
        }
        System.out.println("current backgroundsequence: " + backgroundSequence);
    } //getTextOp


    public void toCalc() {
        if (OPCount == CPCount) {
            if (backgroundSequence.contains("(")) {
                while (backgroundSequence.contains("(")) {
                    indexInnermostOParenthesis = backgroundSequence.lastIndexOf("(");
                    System.out.println("Index of innermost ( is " + indexInnermostOParenthesis);
                    indexInnermostCParenthesis = backgroundSequence.indexOf(")",backgroundSequence.lastIndexOf("("));
                    System.out.println("Index of innermost ) is " + indexInnermostCParenthesis);
                    String testToSolve = backgroundSequence.substring(indexInnermostOParenthesis+1,indexInnermostCParenthesis);
                    System.out.println("First priority to solve: " + testToSolve);


                    if (testToSolve.contains("×") || testToSolve.contains("÷") || testToSolve.contains("%")) {
                        if (testToSolve.contains("×")) {
                            determineOps = determineOps + 2;
                        }

                        if (testToSolve.contains("÷")) {
                            determineOps = determineOps + 3;
                        }

                        if (testToSolve.contains("%")) {
                            determineOps = determineOps + 4;
                        }
                        System.out.println("Current value of determine ops: " + determineOps);
                        switch (determineOps) {
                            case 2:
                                toSolve(testToSolve,"×",true);
                                break;
                            case 3:
                                toSolve(testToSolve,"÷",true);
                                break;
                            case 4:
                                toSolve(testToSolve,"%",true);
                                break;
                            case 5:
                                if (testToSolve.indexOf("×") < testToSolve.indexOf("÷")) {
                                    toSolve(testToSolve,"×",true);
                                }
                                if (testToSolve.indexOf("÷") < testToSolve.indexOf("×")) {
                                    toSolve(testToSolve,"÷",true);
                                }
                                break;
                            case 6:
                                if (testToSolve.indexOf("×") < testToSolve.indexOf("%")) {
                                    toSolve(testToSolve,"×",true);
                                }
                                if (testToSolve.indexOf("%") < testToSolve.indexOf("×")) {
                                    toSolve(testToSolve,"%",true);
                                }
                                break;
                            case 7:
                                if (testToSolve.indexOf("÷") < testToSolve.indexOf("%")) {
                                    toSolve(testToSolve,"÷",true);
                                }
                                if (testToSolve.indexOf("%") < testToSolve.indexOf("÷")) {
                                    toSolve(testToSolve,"%",true);
                                }
                                break;
                            case 9:
                                if (testToSolve.indexOf("×") < testToSolve.indexOf("%") && testToSolve.indexOf("×") < testToSolve.indexOf("÷")) {
                                    toSolve(testToSolve,"×",true);
                                }
                                if (testToSolve.indexOf("%") < testToSolve.indexOf("×") && testToSolve.indexOf("%") < testToSolve.indexOf("÷")) {
                                    toSolve(testToSolve,"%",true);
                                }
                                if (testToSolve.indexOf("÷") < testToSolve.indexOf("×") && testToSolve.indexOf("÷") < testToSolve.indexOf("%")) {
                                    toSolve(testToSolve,"÷",true);
                                }
                                break;
                        }
                    }

                    else if (testToSolve.contains("+")) {
                        toSolve(testToSolve,"+",true);
                    }

                    else {
                        backgroundSequence = backgroundSequence.replace(backgroundSequence.substring(indexInnermostOParenthesis,indexInnermostCParenthesis+1),testToSolve);
                        System.out.println("This was chosen");
                    }

                    determineOps = 0;
                }
                while (backgroundSequence.contains("+") || backgroundSequence.contains("×") || backgroundSequence.contains("÷") || backgroundSequence.contains("%")) {
                    String testToSolve = backgroundSequence;
                    System.out.println("Sequence now solving (1): " + testToSolve);


                    if (testToSolve.contains("×") || testToSolve.contains("÷") || testToSolve.contains("%")) {
                        if (testToSolve.contains("×")) {
                            determineOps = determineOps + 2;
                        }

                        if (testToSolve.contains("÷")) {
                            determineOps = determineOps + 3;
                        }

                        if (testToSolve.contains("%")) {
                            determineOps = determineOps + 4;
                        }
                        System.out.println("Current value of determine ops: " + determineOps);
                        switch (determineOps) {
                            case 2:
                                toSolve(testToSolve,"×",false);
                                break;
                            case 3:
                                toSolve(testToSolve,"÷",false);
                                break;
                            case 4:
                                toSolve(testToSolve,"%",false);
                                break;
                            case 5:
                                if (testToSolve.indexOf("×") < testToSolve.indexOf("÷")) {
                                    toSolve(testToSolve,"×",false);
                                }
                                if (testToSolve.indexOf("÷") < testToSolve.indexOf("×")) {
                                    toSolve(testToSolve,"÷",false);
                                }
                                break;
                            case 6:
                                if (testToSolve.indexOf("×") < testToSolve.indexOf("%")) {
                                    toSolve(testToSolve,"×",false);
                                }
                                if (testToSolve.indexOf("%") < testToSolve.indexOf("×")) {
                                    toSolve(testToSolve,"%",false);
                                }
                                break;
                            case 7:
                                if (testToSolve.indexOf("÷") < testToSolve.indexOf("%")) {
                                    toSolve(testToSolve,"÷",false);
                                }
                                if (testToSolve.indexOf("%") < testToSolve.indexOf("÷")) {
                                    toSolve(testToSolve,"%",false);
                                }
                                break;
                            case 9:
                                if (testToSolve.indexOf("×") < testToSolve.indexOf("%") && testToSolve.indexOf("×") < testToSolve.indexOf("÷")) {
                                    toSolve(testToSolve,"×",false);
                                }
                                if (testToSolve.indexOf("%") < testToSolve.indexOf("×") && testToSolve.indexOf("%") < testToSolve.indexOf("÷")) {
                                    toSolve(testToSolve,"%",false);
                                }
                                if (testToSolve.indexOf("÷") < testToSolve.indexOf("×") && testToSolve.indexOf("÷") < testToSolve.indexOf("%")) {
                                    toSolve(testToSolve,"÷",false);
                                }
                                break;
                        }
                    }


                    else if (testToSolve.contains("+")) {
                        toSolve(testToSolve,"+",false);
                    }
                    determineOps = 0;
                }
                }
            else {
                while (backgroundSequence.contains("+") || backgroundSequence.contains("×") || backgroundSequence.contains("÷") || backgroundSequence.contains("%")) {
                    String testToSolve = backgroundSequence;
                    System.out.println("Sequence now solving (2): " + testToSolve);


                    if (testToSolve.contains("×") || testToSolve.contains("÷") || testToSolve.contains("%")) {
                        if (testToSolve.contains("×")) {
                            determineOps = determineOps + 2;
                        }

                        if (testToSolve.contains("÷")) {
                            determineOps = determineOps + 3;
                        }

                        if (testToSolve.contains("%")) {
                            determineOps = determineOps + 4;
                        }
                        System.out.println("Current value of determine ops: " + determineOps);
                        switch (determineOps) {
                            case 2:
                                toSolve(testToSolve,"×",false);
                                break;
                            case 3:
                                toSolve(testToSolve,"÷",false);
                                break;
                            case 4:
                                toSolve(testToSolve,"%",false);
                                break;
                            case 5:
                                if (testToSolve.indexOf("×") < testToSolve.indexOf("÷")) {
                                    toSolve(testToSolve,"×",false);
                                }
                                if (testToSolve.indexOf("÷") < testToSolve.indexOf("×")) {
                                    toSolve(testToSolve,"÷",false);
                                }
                                break;
                            case 6:
                                if (testToSolve.indexOf("×") < testToSolve.indexOf("%")) {
                                    toSolve(testToSolve,"×",false);
                                }
                                if (testToSolve.indexOf("%") < testToSolve.indexOf("×")) {
                                    toSolve(testToSolve,"%",false);
                                }
                                break;
                            case 7:
                                if (testToSolve.indexOf("÷") < testToSolve.indexOf("%")) {
                                    toSolve(testToSolve,"÷",false);
                                }
                                if (testToSolve.indexOf("%") < testToSolve.indexOf("÷")) {
                                    toSolve(testToSolve,"%",false);
                                }
                                break;
                            case 9:
                                if (testToSolve.indexOf("×") < testToSolve.indexOf("%") && testToSolve.indexOf("×") < testToSolve.indexOf("÷")) {
                                    toSolve(testToSolve,"×",false);
                                }
                                if (testToSolve.indexOf("%") < testToSolve.indexOf("×") && testToSolve.indexOf("%") < testToSolve.indexOf("÷")) {
                                    toSolve(testToSolve,"%",false);
                                }
                                if (testToSolve.indexOf("÷") < testToSolve.indexOf("×") && testToSolve.indexOf("÷") < testToSolve.indexOf("%")) {
                                    toSolve(testToSolve,"÷",false);
                                }
                                break;
                        }
                    }


                    else if (testToSolve.contains("+")) {
                        toSolve(testToSolve,"+",false);
                    }
                    determineOps = 0;
                }
            }
        }
        else {
            Toast.makeText(this,"Grouping Error; please check amount of parentheses",Toast.LENGTH_LONG).show();
        }
        backgroundSequence = txtViewSmall.getText().toString();
        if (txtViewSmall.getText().toString().contains("-")) {
            for (int i=0;i<txtViewSmall.getText().toString().length()-1;i++) {
                if (txtViewSmall.getText().toString().charAt(i) == '-') {
                    backgroundSequence = txtViewSmall.getText().toString().replace(txtViewSmall.getText().toString().substring(i,i+1),"+-");
                }
            }
        }

    }

    public void toClear() {
        String testString = txtViewSmall.getText().toString();
        if (!testString.trim().equals("")) {
           int endCharIndex = txtViewSmall.getText().toString().length() - 1;
           int backgroundEndCharIndex = backgroundSequence.length() - 1;
           if (txtViewSmall.getText().toString().charAt(endCharIndex) == '(') {
               OPCount--;
           }
           if (txtViewSmall.getText().toString().charAt(endCharIndex) == ')') {
               CPCount--;
           }
           String tempString = txtViewSmall.getText().toString().substring(0, endCharIndex);
           String backgroundTempString;
           if (backgroundSequence.endsWith("-")) {
               backgroundTempString = backgroundSequence.substring(0,backgroundEndCharIndex-1);
           }
           else {
               backgroundTempString = backgroundSequence.substring(0,backgroundEndCharIndex);
           }
           backgroundSequence = backgroundTempString;
           txtViewSmall.setText(tempString);


           Toast.makeText(this, "clear in process", Toast.LENGTH_SHORT).show();
           System.out.println("current backgroundsequence: " + backgroundSequence);
           System.out.println("This is the old String:" + tempString);
        }
        else {
            txtViewBig.setText("0".trim());
        }
    }


    public void toCheckParenthesis() {
        boolean endsWithOpCondition = txtViewSmall.getText().toString().endsWith("+") || txtViewSmall.getText().toString().endsWith("-")
                || txtViewSmall.getText().toString().endsWith("×") || txtViewSmall.getText().toString().endsWith("÷")
                || txtViewSmall.getText().toString().endsWith("(") || txtViewSmall.getText().toString().isEmpty();
        if (!hasOpeningParenthesis) {
            if (endsWithOpCondition) {
                String getTextOParenth = txtViewSmall.getText()+"(";
                txtViewSmall.setText(getTextOParenth);
                backgroundSequence = backgroundSequence.trim() + "(";
            }
            else {
                String getTextOParenth = txtViewSmall.getText()+"x(";
                txtViewSmall.setText(getTextOParenth);
                backgroundSequence = backgroundSequence.trim() + "x(";
            }
            OPCount++;
            hasOpeningParenthesis = true;
        }
        else {
            if (endsWithOpCondition) {
                String getTextOParenth = txtViewSmall.getText()+"(";
                txtViewSmall.setText(getTextOParenth);
                backgroundSequence = backgroundSequence.trim() + "(";
                OPCount++;
                hasOpeningParenthesis = true;
            }
            else {
                String getTextCParenth = txtViewSmall.getText()+")";
                txtViewSmall.setText(getTextCParenth);
                backgroundSequence = backgroundSequence.trim() + ")";
                CPCount++;
                if (txtViewSmall.getText().toString().endsWith(")")) {
                    hasOpeningParenthesis = OPCount != CPCount;
                }
                else {
                    hasOpeningParenthesis = false;
                }
            }

        }
    } //toCheckParenthesis()


    public void toSolve(String testToSolve, String op, boolean hasParenthesis) {
        //-- get indexes
        //get current operation
        int operandIndex = testToSolve.indexOf(op);
        System.out.println("Last index of " + op + ": " + operandIndex);

        //check operation/s at left and right of current operation
        if (testToSolve.contains("+") || testToSolve.contains("×") || testToSolve.contains("÷") || testToSolve.contains("%")) {
            for (int i=0;i<operandIndex;i++) {
                if (testToSolve.charAt(i) == '+' || testToSolve.charAt(i) == 'x' || testToSolve.charAt(i) == '/' || testToSolve.charAt(i) == '%') {
                    lastIndex = i;
                }
            }
            for (int i=testToSolve.length()-1;i>operandIndex;i--) {
                if (testToSolve.charAt(i) == '+' || testToSolve.charAt(i) == 'x' || testToSolve.charAt(i) == '÷' || testToSolve.charAt(i) == '%') {
                    nextIndex = i;
                }
            }
        }
        int beforeOperandIndex = lastIndex;
        System.out.println("Index of possible op before current opIndex: " + beforeOperandIndex);
        int afterOperandIndex = nextIndex;
        System.out.println("Index of possible op before after opIndex: " + afterOperandIndex);
        //--

        toSolveHelperMethod(testToSolve,op, hasParenthesis,operandIndex,beforeOperandIndex,afterOperandIndex);

        lastIndex = -1;
        nextIndex = -1;

        if (!backgroundSequence.contains("+") && !backgroundSequence.contains("×") && !backgroundSequence.contains("÷") && !backgroundSequence.contains("%")) {
            if (backgroundSequence.contains(".")) {
                int dotCheck = backgroundSequence.indexOf(".");
                String decimalValue = backgroundSequence.substring(dotCheck+1);
                if (Integer.parseInt(decimalValue) == 0) {
                    backgroundSequence = backgroundSequence.substring(0,dotCheck);
                }
            }
        }
        txtViewBig.setText(backgroundSequence);

    } //toSolve

    public void toSolveHelperMethod(String testToSolve, String op,
                                    boolean hasParenthesis, int operandIndex, int beforeOperandIndex, int afterOperandIndex) {
        System.out.println("Solving under parenthesis");
        //solving w/ respect to the indexes

        System.out.println("Solving under parenthesis, left operand exists");
        float a;
        float b;
        String newEquation = null;
        String newEquation2 = null;
        if (beforeOperandIndex != -1) { //if left operand exists
            a = Float.parseFloat(testToSolve.substring(beforeOperandIndex+1,operandIndex));
            newEquation = testToSolve.substring(0,beforeOperandIndex+1).trim();
            determineCurrentSolved += 1;
        }
        else {
            a = Float.parseFloat(testToSolve.substring(0,operandIndex));
        }
        if (afterOperandIndex != -1) { //if right operand exists
            b = Float.parseFloat(testToSolve.substring(operandIndex+1,afterOperandIndex));
            newEquation2 = testToSolve.substring(afterOperandIndex).trim();
            determineCurrentSolved += 2;
        }
        else {
            b = Float.parseFloat(testToSolve.substring(operandIndex+1));
        }
        switch (op) {
            case "+":
                isSolved = String.valueOf(a+b);
                break;
            case "×":
                isSolved = String.valueOf(a*b);
                break;
            case "÷":
                isSolved = String.valueOf(a/b);
                break;
            case "%":
                isSolved = String.valueOf(a%b);
                break;
        }
        String currentSolved = null;
        switch (determineCurrentSolved) {
            case 0:
                currentSolved = isSolved;
                break;
            case 1:
                currentSolved = newEquation + isSolved;
                break;
            case 2:
                currentSolved = isSolved + newEquation2;
                break;
            case 3:
                currentSolved = newEquation + isSolved + newEquation2;
                break;
        }
        if (hasParenthesis) {
            assert currentSolved != null;
            if (currentSolved.contains("+") || currentSolved.contains("×") || currentSolved.contains("÷") || currentSolved.contains("%")) {
                backgroundSequence = backgroundSequence.replace(backgroundSequence.substring(indexInnermostOParenthesis+1,indexInnermostCParenthesis),currentSolved);
            }
            else {
                backgroundSequence = backgroundSequence.replace(backgroundSequence.substring(indexInnermostOParenthesis,indexInnermostCParenthesis+1),currentSolved);
            }
        }
        else {
            backgroundSequence = currentSolved;
        }
        determineCurrentSolved = 0;

        System.out.println("Current equation: " + backgroundSequence);
    } //toSolveHelperMethod
}