package com.example.dg123.lab8_code;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.WindowDecorActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private List<Map<String, String>> list;
    private Button button;
    private myDB mydb;
    private ListView listView;
    private boolean tag;
    private SimpleAdapter adapter;
    public static Set<String> nameSet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tag = true;
        mydb = new myDB(this, null, null, 0);
        button = (Button) findViewById(R.id.activity_main_add_item);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ItemActivity.class);
                startActivityForResult(intent, 1);
            }
        });
        list = new ArrayList<>();
        nameSet = new HashSet<>();
        getDatas();
        listView = (ListView) findViewById(R.id.activity_main_list);
        adapter = new SimpleAdapter(this, list, R.layout.list_item,
                new String[] {"name", "birthday", "gift"},
                new int[] {R.id.list_item_name, R.id.list_item_birthday, R.id.list_item_gift});
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                if (tag == false) return;
                tag = false;
                Map<String, Object> map = (Map<String, Object>) adapter.getItem(position);
                LayoutInflater factor = LayoutInflater.from(MainActivity.this);
                final View view_in = factor.inflate(R.layout.dialoglayout, null);
                final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("恭喜发财");
                builder.setView(view_in);
                EditText name = (EditText) view_in.findViewById(R.id.dialoglayout_name_edit);
                name.setText(map.get("name").toString().trim());
                EditText birthday = (EditText) view_in.findViewById(R.id.dialoglayout_birthday_edit);
                birthday.setText(map.get("birthday").toString().trim());
                EditText gift = (EditText) view_in.findViewById(R.id.dialoglayout_gift_edit);
                gift.setText(map.get("gift").toString().trim());
                TextView phone = (TextView) view_in.findViewById(R.id.dialoglayout_phone_edit);
                if (map.get("phone").toString().length() == 0)
                    phone.setText("无");
                else
                    phone.setText(map.get("phone").toString().trim());
                builder.setNegativeButton("放弃修改", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setPositiveButton("保存修改", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Map<String, Object> map = (Map<String, Object>) adapter.getItem(position);
                        EditText name = (EditText) view_in.findViewById(R.id.dialoglayout_name_edit);
                        String s = map.get("name").toString().trim();
                        //判断修改信息有没有出现姓名重复
                        if (name.getText().toString().trim().length() > 0 && (!nameSet.contains(name.getText().toString().trim()) || s.equals(name.getText().toString().trim()))) {
                            map.put("name", name.getText().toString().trim());
                            EditText birthday = (EditText) view_in.findViewById(R.id.dialoglayout_birthday_edit);
                            map.put("birthday", birthday.getText().toString().trim());
                            EditText gift = (EditText) view_in.findViewById(R.id.dialoglayout_gift_edit);
                            map.put("gift", gift.getText().toString().trim());
                            String phoneNumber = findPhone(name.getText().toString().trim());
                            map.put("phone", phoneNumber);
                            adapter.notifyDataSetChanged();
                            mydb.update(s, map.get("name").toString().trim(),
                                    map.get("birthday").toString().trim(), map.get("gift").toString().trim());
                            nameSet.remove(s);
                            nameSet.add(name.getText().toString().trim());
                        } else{
                            if (name.getText().toString().trim().length() > 0)
                                Toast.makeText(MainActivity.this, "名字重复啦，请检查", Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(MainActivity.this, "名字为空，请完善", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                Dialog dialog = builder.create();
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        tag = true;
                    }
                });
                dialog.show();
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                if (tag == false) return false;
                tag = false;
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("是否删除？");
                builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        mydb.delete(list.get(position).get("name").toString().trim());
                        nameSet.remove(list.get(position).get("name").toString().trim());
                        list.remove(position);
                        adapter.notifyDataSetChanged();
                    }
                });
                Dialog dialog = builder.create();
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        tag = true;
                    }
                });
                dialog.show();
                return false;
            }
        });
    }

    public void getDatas() {
        List<Map<String, String> >data = mydb.query();
        list.clear();
        nameSet = new HashSet<>();
        for (int i=0;i<data.size();++i) {
            Map<String, String> map = data.get(i);
            map.put("phone", findPhone(map.get("name").trim()));
            list.add(map);
            nameSet.add(map.get("name").trim());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String name = bundle.getString("name");
            if (!nameSet.contains(name)) {
                nameSet.add(name);
                String birthday = bundle.getString("birthday");
                String gift = bundle.getString("gift");
                mydb.insert(name, birthday, gift);
                Map<String, String> map = new HashMap<>();
                map.put("name", name);
                map.put("birthday", birthday);
                map.put("gift", gift);
                map.put("phone", findPhone(name));
                list.add(map);
                adapter.notifyDataSetChanged();
            }
        }
    }

    public String findPhone(String name) {
        String number = "";
        Cursor cursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);

        while (cursor != null && cursor.moveToNext()) {
            int isHas = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract
                    .Contacts.HAS_PHONE_NUMBER)));

            if (isHas > 0) {
                String nname = cursor.getString(cursor.getColumnIndex(ContactsContract
                        .Contacts.DISPLAY_NAME));
                if (!nname.equals(name)) continue;
                String contactID = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                Cursor phone = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + contactID, null, null);
                while (phone != null && phone.moveToNext()) {
                    number += phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds
                        .Phone.NUMBER)) + " ";
                }

            }

        }
        return number;
    }
}
