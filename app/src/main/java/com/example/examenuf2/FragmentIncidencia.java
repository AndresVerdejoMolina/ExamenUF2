package com.example.examenuf2;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentIncidencia.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentIncidencia#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentIncidencia extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ListView mIncidenciasList;
    private AdapterIncidencia mIncidenciasAdapter;

    public ArrayList<Incidencia> incidencias;

    private OnFragmentInteractionListener mListener;

    public FragmentIncidencia() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static FragmentIncidencia newInstance() {
        FragmentIncidencia fragment = new FragmentIncidencia();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        incidencias = new ArrayList<Incidencia>();
    }
    public void addIncidencia(Incidencia incidencia){
        incidencias.add(incidencia);
        mIncidenciasAdapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment_incidencia, container, false);
        mIncidenciasList = (ListView) view.findViewById(R.id.incidencias_list);
        mIncidenciasAdapter = new AdapterIncidencia(getActivity(), incidencias);

        mIncidenciasList.setAdapter(mIncidenciasAdapter);

        mIncidenciasList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Incidencia currentCancion= mIncidenciasAdapter.getItem(position);

                Toast.makeText(getActivity(),
                        "Cambiando estado",
                        Toast.LENGTH_SHORT).show();


            }
        });

        return view;
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
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
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
