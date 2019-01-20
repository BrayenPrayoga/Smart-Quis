package com.brayenprayoga.quiscahlemper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.brayenprayoga.quiscahlemper.QuisContract.*;

import java.util.ArrayList;
import java.util.List;

public class QuisDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Quis.db";
    private static final int DATABASE_VERSION = 2;

    private SQLiteDatabase db;

    public QuisDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;

        final String SQL_CREATE_QUESTIONS_TABLE = "CREATE TABLE " +
                QuestionsTable.TABLE_NAME + " ( " +
                QuestionsTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                QuestionsTable.COLUMN_QUESTION + " TEXT, " +
                QuestionsTable.COLUMN_OPTION1 + " TEXT, " +
                QuestionsTable.COLUMN_OPTION2 + " TEXT, " +
                QuestionsTable.COLUMN_OPTION3 + " TEXT, " +
                QuestionsTable.COLUMN_ANSWER_NR + " INTEGER, " +
                QuestionsTable.COLUMN_DIFFICULTY + " TEXT " +
                ")";

        db.execSQL(SQL_CREATE_QUESTIONS_TABLE);
        fillQuestionsTable();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + QuestionsTable.TABLE_NAME);
        onCreate(db);
    }

    private void fillQuestionsTable() {
        //SOAL MEDIUM
        Question q1 = new Question("Jika 200 - 45 = ( 3 x n ) + 35, maka nilai n adalah...",
                "40", "45", "50", 1, Question.DIFFICULTY_MEDIUM);
        addQuestion(q1);
        Question q2 = new Question("Nilai dari 35 : (-5) + 10 - (-3) adalah...",
                "4", "5", "6", 3, Question.DIFFICULTY_MEDIUM);
        addQuestion(q2);
        Question q3 = new Question("Faktorisasai prima dari 2.450 adalah...",
                "2 x 5² x 7", "2² x 5 x 7", "2 x 5² x 7²", 3, Question.DIFFICULTY_MEDIUM);
        addQuestion(q3);
        Question q4 = new Question("FPB dari 72, 96, dan 132 adalah....",
                "12", "44", "288", 1, Question.DIFFICULTY_MEDIUM);
        addQuestion(q4);
        Question q5 = new Question("Faktor prima dari 300 adalah...",
                "1, 2", "2, 3", "2, 3, 5", 3, Question.DIFFICULTY_MEDIUM);
        addQuestion(q5);
        Question q6 = new Question("Hasil dari 50 + 10 x 65 - 225 : 5 adalah....",
                "635", "655", "685", 2, Question.DIFFICULTY_MEDIUM);
        addQuestion(q6);
        Question q7 = new Question("Hasil dari -175 + 19 x 7 - (-28) adalah....",
                "-14", "14", "-27", 1, Question.DIFFICULTY_MEDIUM);
        addQuestion(q7);
        Question q8 = new Question("Hasil pengerjaan dari 35² - 24² adalah....",
                "649", "676", "1.201", 1, Question.DIFFICULTY_MEDIUM);
        addQuestion(q8);


        //SOAL HARD
        Question q9 = new Question("Perbandingan panjang dan lebar persegi panjang adalah 4 : 3. Jika kelilingnya 84 cm, luasnya adalah….",
                "234 cm2", "432 cm2", "452 cm2", 2, Question.DIFFICULTY_HARD);
        addQuestion(q9);
        Question q10 = new Question("Diberikan P = {1,2,3,4,5,6,7,8,9}. Himpunan bilangan ganjil yang terdapat di P adalah....",
                "{1,3,5,7,9}", "{2,4,8,9}", "{1,5,7,9}", 1, Question.DIFFICULTY_HARD);
        addQuestion(q10);
        Question q11 = new Question("Hasil dari (- 12) : 3 + 8 X (- 5) adalah...",
                "-44", "-36", "28", 1, Question.DIFFICULTY_HARD);
        addQuestion(q11);
        Question q12 = new Question("7arak sebenarnya antara dua kota 80 km, sedangkan jarak pada peta 5 cm. skala peta tersebut adalah...",
                "1 : 600", "1 : 160.000", "1 : 1.600.000", 3, Question.DIFFICULTY_HARD);
        addQuestion(q12);
        Question q13 = new Question("Dengan harga jual Rp 9.000.000 seorang pedagang rugi 10% .Harga pembeliannya adalah...",
                "10.000.000", "9.900.000", "8.100.000", 1, Question.DIFFICULTY_HARD);
        addQuestion(q13);
        Question q14 = new Question(";umus suku ke-n barisan bilangan 20, 17, 19, 11, ... adalah...",
                "17 + 3n", "23 - 3n", "23n - 3", 2, Question.DIFFICULTY_HARD);
        addQuestion(q14);
        Question q15 = new Question("Gradien garis dengan persamaan 4x - 2y + 8 = 0 adalah...",
                "2", "1/2", "-1/2", 1, Question.DIFFICULTY_HARD);
        addQuestion(q15);
        Question q16 = new Question("Persamaan garis melalui titik (-4, 2) dan tegak lurus dengan garis 2x+6y-12=0 adalah",
                "3y = x - 2", "3y = -x - 10", "y = 3x + 10", 3, Question.DIFFICULTY_HARD);
        addQuestion(q16);

        //SOAL EASY
        Question q17 = new Question("Hasil dari 20 + 83 - 13 adalah...",
                "90", "95", "100", 1, Question.DIFFICULTY_EASY);
        addQuestion(q17);
        Question q18 = new Question("Hasil dari 2 + 2 - 100 + 225 x 0 adalah",
                "18", "9", "0", 3, Question.DIFFICULTY_EASY);
        addQuestion(q18);
        Question q19 = new Question("Harga 1 telur adalah Rp2000. Berapa harga 8 butir telur?",
                "14000", "16000", "18000", 2, Question.DIFFICULTY_EASY);
        addQuestion(q19);
        Question q20 = new Question("Budi mempunyai 10 kelereng, Ani mempunyai 7 kelereng. Berapa kelereng Beni?",
                "10", "17", "Tidak Diketahui", 3, Question.DIFFICULTY_EASY);
        addQuestion(q20);
        Question q21 = new Question("Burhan dikasih uang oleh ibu nya Rp10.000, dibelikan Es Rp 3000. berapa kembalian yang diterima Burhan?",
                "6000", "7000", "8000", 2, Question.DIFFICULTY_EASY);
        addQuestion(q21);
        Question q22 = new Question("Uang yang dimiliki Joko adalah Rp7000. ia membeli 3 permen dengan harga Rp1500/buah. berapa yang harus dibayar Joko?",
                "4000", "4500", "5000", 2, Question.DIFFICULTY_EASY);
        addQuestion(q22);
        Question q23 = new Question("Jumlah dari 112 + 211 + 122 adalah...",
                "445", "450", "455", 1, Question.DIFFICULTY_EASY);
        addQuestion(q23);
        Question q24 = new Question("Hasil Penjumlahan 300 + 200 + 500 - 500 adalah...",
                "300", "400", "500", 3, Question.DIFFICULTY_EASY);
        addQuestion(q24);
    }

    private void addQuestion(Question question) {
        ContentValues cv = new ContentValues();
        cv.put(QuestionsTable.COLUMN_QUESTION, question.getQuestion());
        cv.put(QuestionsTable.COLUMN_OPTION1, question.getOption1());
        cv.put(QuestionsTable.COLUMN_OPTION2, question.getOption2());
        cv.put(QuestionsTable.COLUMN_OPTION3, question.getOption3());
        cv.put(QuestionsTable.COLUMN_ANSWER_NR, question.getAnswerNr());
        cv.put(QuestionsTable.COLUMN_DIFFICULTY, question.getDifficulty());
        db.insert(QuestionsTable.TABLE_NAME, null, cv);
    }

    public ArrayList<Question> getAllQuestions() {
        ArrayList<Question> questionList = new ArrayList<>();
        db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + QuestionsTable.TABLE_NAME, null);

        if (c.moveToFirst()) {
            do {
                Question question = new Question();
                question.setQuestion(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_QUESTION)));
                question.setOption1(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION1)));
                question.setOption2(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION2)));
                question.setOption3(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION3)));
                question.setAnswerNr(c.getInt(c.getColumnIndex(QuestionsTable.COLUMN_ANSWER_NR)));
                question.setDifficulty(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_DIFFICULTY)));
                questionList.add(question);
            } while (c.moveToNext());
        }

        c.close();
        return questionList;
    }

    public ArrayList<Question> getQuestions(String difficulty) {
        ArrayList<Question> questionList = new ArrayList<>();
        db = getReadableDatabase();

        String[] selectionArgs = new String[]{difficulty};
        Cursor c = db.rawQuery("SELECT * FROM " + QuestionsTable.TABLE_NAME +
                " WHERE " + QuestionsTable.COLUMN_DIFFICULTY + " = ?", selectionArgs);

        if (c.moveToFirst()) {
            do {
                Question question = new Question();
                question.setQuestion(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_QUESTION)));
                question.setOption1(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION1)));
                question.setOption2(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION2)));
                question.setOption3(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION3)));
                question.setAnswerNr(c.getInt(c.getColumnIndex(QuestionsTable.COLUMN_ANSWER_NR)));
                question.setDifficulty(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_DIFFICULTY)));
                questionList.add(question);
            } while (c.moveToNext());
        }

        c.close();
        return questionList;
    }
}
