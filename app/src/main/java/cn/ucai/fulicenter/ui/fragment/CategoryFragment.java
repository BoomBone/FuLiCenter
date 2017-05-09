package cn.ucai.fulicenter.ui.fragment;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.data.bean.CategoryChildBean;
import cn.ucai.fulicenter.data.bean.CategoryGroupBean;
import cn.ucai.fulicenter.data.net.GoodsModel;
import cn.ucai.fulicenter.data.net.IGoodsModel;
import cn.ucai.fulicenter.data.net.OnCompleteListener;
import cn.ucai.fulicenter.data.utils.L;
import cn.ucai.fulicenter.data.utils.ResultUtils;
import cn.ucai.fulicenter.ui.adapter.CategoryAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryFragment extends Fragment {
    private static final String TAG = "CategoryFragment";

    Unbinder unbinder;

    IGoodsModel model;
    CategoryAdapter adapter;

    ProgressDialog pd;
    ArrayList<CategoryGroupBean> groupList = new ArrayList<>();
    List<List<CategoryChildBean>> childList = new ArrayList<>();
    int groupCount = 0;
    @BindView(R.id.elv_category)
    ExpandableListView elvCategory;
    @BindView(R.id.tv_nomore)
    TextView tvNomore;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, null);
        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        model = new GoodsModel();
        initDialog();
        loadData();
    }


    private void initDialog() {
        pd = new ProgressDialog(getContext());
        pd.setMessage(getString(R.string.load_more));
        pd.show();
    }

    private void loadData() {

        model.loadCreategoryGroup(getContext(), new OnCompleteListener<CategoryGroupBean[]>() {
            @Override
            public void onSuccess(CategoryGroupBean[] result) {
                L.e(TAG, "loadCreategoryGroup_resule=" + Arrays.toString(result));
                if (result != null) {
                    groupList = ResultUtils.array2List(result);
                    if(childList!=null){
                        childList.clear();
                    }
                    for(int i=0;i<groupList.size();i++){
                        childList.add(new ArrayList<CategoryChildBean>());
                        loadChildData(groupList.get(i).getId(),i);
                    }
                }
            }

            @Override
            public void onError(String error) {
                pd.dismiss();
                setListVisibility(false);
            }
        });
    }

    private void loadChildData(int parentId, final int index) {
        model.loadCreategoryChild(getContext(), parentId, new OnCompleteListener<CategoryChildBean[]>() {
            @Override
            public void onSuccess(CategoryChildBean[] result) {
                L.e(TAG, "loadChildData_resule=" + Arrays.toString(result));
                groupCount++;

                if (result != null) {
                    ArrayList<CategoryChildBean> list = ResultUtils.array2List(result);
                    //childList.add(list);
                    childList.set(index, list);
                }
                if (groupCount == groupList.size()) {
                    pd.dismiss();
                    setListVisibility(true);
                    updataUI();
                }
            }

            @Override
            public void onError(String error) {
                groupCount++;
                if (groupCount == groupList.size()) {
                    pd.dismiss();
                    setListVisibility(false);
                }

            }
        });

    }

    private void updataUI() {
        if (adapter == null) {
            adapter = new CategoryAdapter(groupList, childList, getContext());
            elvCategory.setAdapter(adapter);
        }
    }


    //刷新页面能看见，加载页面就看不见
    void setListVisibility(boolean visibility) {
        elvCategory.setVisibility(visibility ? View.VISIBLE : View.GONE);
        tvNomore.setVisibility(visibility ? View.GONE : View.VISIBLE);
    }

    @OnClick(R.id.tv_nomore)
    public void reload() {
        pd.show();
        loadData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (adapter != null) {
            unbinder.unbind();
        }
    }

}
