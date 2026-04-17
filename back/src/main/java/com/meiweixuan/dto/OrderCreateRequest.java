package com.meiweixuan.dto;

import javax.validation.constraints.NotBlank;
import java.util.Objects;

public class OrderCreateRequest {

    @NotBlank(message = "收货人不能为空")
    private String name;

    @NotBlank(message = "联系电话不能为空")
    private String phone;

    @NotBlank(message = "配送地址不能为空")
    private String address;

    private String note;

    @NotBlank(message = "支付方式不能为空")
    private String paymentMethod;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderCreateRequest that = (OrderCreateRequest) o;
        return Objects.equals(name, that.name)
                && Objects.equals(phone, that.phone)
                && Objects.equals(address, that.address)
                && Objects.equals(note, that.note)
                && Objects.equals(paymentMethod, that.paymentMethod);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, phone, address, note, paymentMethod);
    }
}
