package project250.cse250.fhb369.ashepashe;

import android.content.Context;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentGENSearch.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentGENSearch#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentGENSearch extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public FragmentGENSearch() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentGENSearch.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentGENSearch newInstance(String param1, String param2) {
        FragmentGENSearch fragment = new FragmentGENSearch();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    String catagoryIndex=null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_fragment_gensearch, container, false);

        EditText search = v.findViewById(R.id.search_edit_text);
        Spinner spinner = v.findViewById(R.id.search_catagory);
        RecyclerView recyclerView = v.findViewById(R.id.search_recycler);
        Button Thesearch = v.findViewById(R.id.search_btn);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.search_catagory, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.hasFixedSize();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:{
                        catagoryIndex = "ALL";
                        return;
                    }
                    case 1:{
                        catagoryIndex = "REPAIR";
                        return;
                    }
                    case 2:{
                        catagoryIndex = "LAUNDRY";
                        return;
                    }
                    case 3:{
                        catagoryIndex = "TRANSPORT";
                        return;
                    }
                    case 4:{
                        catagoryIndex = "FOOD";
                        return;
                    }
                    case 5:{
                        catagoryIndex = "CLEANING";
                        return;
                    }
                    case 6:{
                        catagoryIndex = "SHIFTING";
                        return;
                    }
                    case 7:{
                        catagoryIndex = "HEALTH CARE";
                        return;
                    }
                    case 8:{
                        catagoryIndex = "OTHERS";
                        return;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        Thesearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String src = search.getText().toString().trim();
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Ads");
                String UID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                if(src!=null && catagoryIndex!=null){
                    SearchAdapter adapter = new SearchAdapter(getActivity(), ref, UID, src, catagoryIndex);
                    recyclerView.setAdapter(adapter);
                }
            }
        });

        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
