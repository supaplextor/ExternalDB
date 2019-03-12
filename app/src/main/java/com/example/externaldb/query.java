package com.example.externaldb;

import android.content.Intent;
import android.os.StrictMode;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;

import de.codecrafters.tableview.TableDataAdapter;
import de.codecrafters.tableview.TableHeaderAdapter;
import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.LongPressAwareTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

public class query extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_query, menu);
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            final View rootView = inflater.inflate(R.layout.fragment_query, container, false);
            final TextView tvQuery = (TextView) rootView.findViewById(R.id.querytext);
            String sql = "SELECT * FROM tags";
            tvQuery.setText(sql);
            TextView status = (TextView) rootView.findViewById(R.id.status);
            Intent intent = getActivity().getIntent();
            String driverName = intent.getStringExtra("driverName");
            String jdbc_s   = intent.getStringExtra("jdbc_s");
            status.setText("Attempting to connect to " + jdbc_s + " with driver " + driverName);
            Button querybtn = (Button) rootView.findViewById(R.id.button);

            querybtn.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    Intent intent = getActivity().getIntent();
                    String driverName = intent.getStringExtra("driverName");
                    String jdbc_s   = intent.getStringExtra("jdbc_s");
                    String username = intent.getStringExtra("username");
                    String password = intent.getStringExtra("password");
                    Connection connection = null;

                    Log.d("driverName is ", driverName);
                    try {
                        Class.forName(driverName).newInstance();

                        connection = DriverManager.getConnection(jdbc_s, username, password);
                        System.out.println("Connected to " + jdbc_s);
                        TextView status = (TextView) rootView.findViewById(R.id.status);
                        status.setText("Connected to " + jdbc_s);
                        // Meta data
                        DatabaseMetaData meta = connection.getMetaData();
                        System.out.println("\nDriver Information");
                        System.out.println("Driver Name: "
                                + meta.getDriverName());
                        System.out.println("Driver Version: "
                                + meta.getDriverVersion());
                        System.out.println("\nDatabase Information ");
                        System.out.println("Database Name: "
                                + meta.getDatabaseProductName());
                        System.out.println("Database Version: "+
                                meta.getDatabaseProductVersion());
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println(e.getMessage());
                        TextView status = (TextView) rootView.findViewById(R.id.status);
                        status.setText(status.getText() + " \n" + e.getMessage());
                        return;
                    }

                    try {
                        final TextView textView = (TextView) rootView.findViewById(R.id.querytext);

                        Statement stmt;
                        String sql = textView.getText().toString();
                        stmt = connection.createStatement(java.sql.ResultSet.TYPE_SCROLL_INSENSITIVE,
                                ResultSet.CONCUR_READ_ONLY);

                        ResultSet rs = stmt.executeQuery(sql);
                        ResultSetMetaData rsmd = rs.getMetaData();
                        int columnsNumber = rsmd.getColumnCount();

                        // Go to the last row
                        rs.last();
                        int numRows = rs.getRow();
                        // Reset row before iterating to get data
                        rs.beforeFirst();

                        String cell_headers[] = new String[columnsNumber];
                        String cells[][] = new String[numRows][columnsNumber];
                        int i = 0;

                        while (rs.next()) {
                            if (i == 0) {
                                for (int j = 1; j <= columnsNumber; j++) {
                                    cell_headers[j-1]=rsmd.getColumnName(j);
                                }
                            }
                            i++;
                            for (int j = 1; j <= columnsNumber; j++) {
                             //   System.out.println(rs.getString(j));
                                cells[i-1][j-1]=rs.getString(j);
                            }
                        }
                        rs.close();
                        stmt.close();

                        TableView<String[]> table = rootView.findViewById(R.id.gv);
                        TableHeaderAdapter myHeaderAdapter =
                                new SimpleTableHeaderAdapter(getContext(), cell_headers);
                        TableDataAdapter<String[]> myDataAdapter =
                                new SimpleTableDataAdapter(getContext(), cells);
                        table.setHeaderAdapter(myHeaderAdapter);
                        table.setColumnCount(columnsNumber);
                        table.setDataAdapter(myDataAdapter);
                        table.setDataAdapter(new SimpleTableDataAdapter(getActivity(), cells));
                        myDataAdapter.notifyDataSetChanged();

                    } catch (java.sql.SQLException e) {
                        e.printStackTrace();
                        final TextView textView = (TextView) rootView.findViewById(R.id.querytext);
                        String sql = textView.getText().toString();
                        System.out.println("SQL Query: " + sql);
                        System.out.println(e.getCause() + e.getMessage());
                        TextView status = (TextView) rootView.findViewById(R.id.status);

                        status.setText(status.getText() + " \n" + e.getCause() + "\n" + e.getMessage());

                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println(e.getCause());
                        TextView status = (TextView) rootView.findViewById(R.id.status);

                        status.setText(status.getText() + " \n" + e.getCause() + "\n");
                    }


                }
            });
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
    }
}
