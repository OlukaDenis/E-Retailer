package com.jusutech.easyshopugserver.Model;

public class Distributor {
        private String Name, Phone, Email, Address, Image;

        public Distributor() {
        }

        public Distributor(String name, String phone, String email, String address, String image) {
            this.Name = name;
            this.Phone = phone;
            this.Email = email;
            this.Address = address;
            this.Image = image;
        }

        public String getName() {
            return Name;
        }

        public void setName(String name) {
            Name = name;
        }

        public String getPhone() {
            return Phone;
        }

        public void setPhone(String phone) {
            Phone = phone;
        }

        public String getEmail() {
            return Email;
        }

        public void setEmail(String email) {
            Email = email;
        }

        public String getAddress() {
            return Address;
        }

        public void setAddress(String address) {
            Address = address;
        }

        public String getImage() {
            return Image;
        }

        public void setImage(String image) {
            Image = image;
        }
}
