package com.example.keep_exploring.helpers;

import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.keep_exploring.DAO.DAO_Address;
import com.example.keep_exploring.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Helper_Post {
    private Context context;
    private Helper_SP helper_sp;
    private String additionalAddress, addressSubmit, categorySubmit;
    private DAO_Address dao_address;

    public Helper_Post(Context context, String additionalAddress, String addressSubmit, String categorySubmit) {
        this.context = context;
        this.additionalAddress = additionalAddress;
        this.addressSubmit = addressSubmit;
        this.categorySubmit = categorySubmit;
        dao_address = new DAO_Address(context);
        helper_sp = new Helper_SP(context);
    }


    public void dialogAddAddress(TextView tvAddress, TextView tvCategory) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_address);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        List<String> dProvinceList = new ArrayList<>();
        List<String> dDistrictList = new ArrayList<>();
        List<String> dWardList = new ArrayList<>();
        List<String> dCategoryList = new ArrayList<>();
        dCategoryList.add("food");
        dCategoryList.add("hotel");
        dCategoryList.add("check_in");
        dProvinceList.addAll(helper_sp.getProvinceList());
        TextView dTvAddress = (TextView) dialog.findViewById(R.id.dAddress_tvAddress);
        TextView dTvCategory = (TextView) dialog.findViewById(R.id.dAddress_tvCategory);

        Spinner dSpCategory = (Spinner) dialog.findViewById(R.id.dAddress_spCategory);
        Spinner dSpProvince = (Spinner) dialog.findViewById(R.id.dAddress_spProvince);
        Spinner dSpDistrict = (Spinner) dialog.findViewById(R.id.dAddress_spDistrict);
        Spinner dSpWard = (Spinner) dialog.findViewById(R.id.dAddress_spWard);

        Button dBtnSubmit = (Button) dialog.findViewById(R.id.dAddress_btnSubmit);
        Button dBtnCancel = (Button) dialog.findViewById(R.id.dAddress_btnCancel);

        EditText dEdtAdditional = (EditText) dialog.findViewById(R.id.dAddress_edtAdditional);

        dEdtAdditional.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()) {
                    additionalAddress = addressSubmit;
                } else {
                    additionalAddress = s + ", " + addressSubmit;
                }
                dTvAddress.setText(additionalAddress);

            }
        });

        setSpinner(dCategoryList, dSpCategory);
        setSpinner(dProvinceList, dSpProvince);

        dSpProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                dao_address.getAddressList(dProvinceList.get(position), "", new Helper_Callback() {
                    @Override
                    public void successReq(Object response) {
                        Map<String, List<String>> map = (Map<String, List<String>>) response;
                        dDistrictList.clear();
                        dWardList.clear();
                        dDistrictList.addAll(map.get("districtList"));
                        dWardList.addAll(map.get("wardList"));
                        setSpinner(dDistrictList, dSpDistrict);
                        setSpinner(dWardList, dSpWard);
                        setAddressToDisPLay(
                                ""
                                , "",
                                dSpProvince.getSelectedItem().toString()
                        );
                        dEdtAdditional.setText("");
                        dTvAddress.setText(addressSubmit);
                    }

                    @Override
                    public void failedReq(String msg) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        dSpDistrict.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                dao_address.getAddressList(
                        dSpProvince.getSelectedItem().toString(),
                        dDistrictList.get(position),
                        new Helper_Callback() {
                            @Override
                            public void successReq(Object response) {
                                Map<String, List<String>> map = (Map<String, List<String>>) response;
                                dWardList.clear();
                                dWardList.addAll(map.get("wardList"));
                                setSpinner(dWardList, dSpWard);
                                setAddressToDisPLay(
                                        dSpWard.getSelectedItem().toString()
                                        , dSpDistrict.getSelectedItem().toString(),
                                        dSpProvince.getSelectedItem().toString()
                                );
                                dEdtAdditional.setText("");
                                dTvAddress.setText(addressSubmit);
                            }

                            @Override
                            public void failedReq(String msg) {

                            }
                        });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        dSpWard.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setAddressToDisPLay(
                        dSpWard.getSelectedItem().toString()
                        , dSpDistrict.getSelectedItem().toString(),
                        dSpProvince.getSelectedItem().toString()
                );
                dEdtAdditional.setText("");
                dTvAddress.setText(addressSubmit);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        dSpCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                categorySubmit = dCategoryList.get(position);
                dTvCategory.setText(categorySubmit);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        dBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dBtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addressSubmit = dTvAddress.getText().toString();
                tvAddress.setText(addressSubmit);
                tvCategory.setText(categorySubmit);
                dialog.dismiss();
            }
        });

        dialog.show();
    }
    private void setSpinner(List<String> spinnerList, Spinner spinner) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                context,
                android.R.layout.simple_dropdown_item_1line,
                spinnerList);
        spinner.setAdapter(adapter);
    }
    private void setAddressToDisPLay(String ward, String district, String province) {
        String sWard;
        String sDistrict;
        additionalAddress = "";
        if (!ward.isEmpty()) {
            sWard = ward + ", ";
        } else {
            sWard = "";
        }
        if (!district.isEmpty()) {
            sDistrict = district + ", ";
        } else {
            sDistrict = "";
        }
        addressSubmit = additionalAddress + sWard + sDistrict + province;
    }
}
