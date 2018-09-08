package com.example.letic.cinqproject;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.letic.cinqproject.adapters.UserAdapter;
import com.example.letic.cinqproject.database.DatabaseHelper;
import com.example.letic.cinqproject.models.User;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class HomeFragment extends Fragment implements OnItemClick {

    private TextView tvName;
    private RecyclerView recyclerViewUsers;
    private EditText search;

    private List<User> users;
    private UserAdapter userAdapter;
    private DatabaseHelper databaseHelper;
    private String emailFromIntent;
    private FloatingActionButton fabAdd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
        setupRecyclerView();

        emailFromIntent = getActivity().getIntent().getStringExtra("NAME");
        tvName.setText(getString(R.string.welcome) + emailFromIntent);

        getDataFromSQLite();
        setListener();
    }

    public void init() {
        users = new ArrayList<>();
        userAdapter = new UserAdapter(users, this);
        recyclerViewUsers = getActivity().findViewById(R.id.recyclerViewUsersList);
        tvName = getActivity().findViewById(R.id.textViewName);
        search = getActivity().findViewById(R.id.search);
        fabAdd = getActivity().findViewById(R.id.fabAddUser);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent accountsIntent = new Intent(getActivity(), RegisterActivity.class);
                accountsIntent.putExtra("ADD", "add_user");
                startActivity(accountsIntent);
            }
        });
    }

    public void setupRecyclerView() {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerViewUsers.setLayoutManager(mLayoutManager);
        recyclerViewUsers.setItemAnimator(new DefaultItemAnimator());
        recyclerViewUsers.setHasFixedSize(true);
        recyclerViewUsers.setAdapter(userAdapter);
        databaseHelper = new DatabaseHelper(getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
        getDataFromSQLite();
    }

    @SuppressLint("StaticFieldLeak")
    private void getDataFromSQLite() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                users.clear();
                users.addAll(databaseHelper.getAllUsers());
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                userAdapter.notifyDataSetChanged();
            }
        }.execute();
    }

    public void setListener() {
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                fillUsers(editable.toString());
            }
        });
    }

    public void fillUsers(String filter) {
        List<User> usersFiltered = (List<User>) ((ArrayList<User>) users).clone();
        if (!"".equals(filter)) {
            for (Iterator<User> it = usersFiltered.iterator(); it.hasNext(); ) {
                if (!it.next().getName().toLowerCase().contains(filter.toLowerCase()))
                    it.remove();
            }
        }
        recyclerViewUsers.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayout.VERTICAL, false));
        recyclerViewUsers.setAdapter(new UserAdapter(usersFiltered, this));
    }

    @Override
    public void recyclerviewOnClick(final int position) {
        AlertDialog.Builder optionsDialog = new AlertDialog.Builder(getActivity());
        optionsDialog.setTitle(getString(R.string.select));
        String[] pictureDialogItems = {
                getString(R.string.edit_user),
                getString(R.string.remove_user)};
        optionsDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                editUser(users.get(position));
                                break;
                            case 1:
                                deleteConfirm(users.get(position), position);
                                break;
                        }
                    }
                });
        optionsDialog.show();
    }

    public void removeUser(User user, int position) {
        if (!user.getEmail().equals(emailFromIntent)) {
            databaseHelper.deleteUser(user);
            users.remove(position);
            userAdapter.notifyItemRemoved(position);
            userAdapter.notifyItemRangeChanged(position, users.size());
            userAdapter.notifyDataSetChanged();
        } else {
            Toast.makeText(getActivity(), getString(R.string.error_remove_user), Toast.LENGTH_LONG).show();
        }
    }

    public void editUser(User user) {
        Intent accountsIntent = new Intent(getActivity(), RegisterActivity.class);
        accountsIntent.putExtra("EMAIL", user.getEmail());
        accountsIntent.putExtra("NAME", user.getName());
        accountsIntent.putExtra("PASSWORD", user.getPassword());
        startActivity(accountsIntent);
    }

    public void deleteConfirm(final User user, final int position) {
        final AlertDialog.Builder optionsDialog = new AlertDialog.Builder(getActivity());
        optionsDialog.setTitle("Deseja realmente excluir este usuário?");
        String[] pictureDialogItems = {
                "Remover",
                "Cancelar"};
        optionsDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                removeUser(user, position);
                                break;
                        }
                    }
                });
        optionsDialog.show();
    }
}
