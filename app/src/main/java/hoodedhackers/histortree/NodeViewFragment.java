package hoodedhackers.histortree;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class NodeViewFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    ////A
    private ArrayAdapter<String> arrayAdapter;
    private TextView nodeName;
    private ListView listView;
    private View rootView;
    private Node node;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static NodeViewFragment newInstance(int sectionNumber) {
        NodeViewFragment fragment = new NodeViewFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public void setNode(Node newNode) {
        node = newNode;
    }

    public void setText() {
        nodeName.setText(node.name);
        arrayAdapter.clear();
        arrayAdapter.addAll(node.vals);
        arrayAdapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_main, container, false);
        nodeName = (TextView) rootView.findViewById(R.id.nodeName);
        listView = (ListView) rootView.findViewById(R.id.listView);
        arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1);
        listView.setAdapter(arrayAdapter);
        setText();
        return rootView;
    }
}