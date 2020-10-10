package com.beatrix.data;

/**
 * @author Beatrice V.
 * @created 22.09.2020 - 13:43
 * @project NetworkLab1
 * In this class are the fields from the server's "database".
 * Getters and setters are used by jackson library which are used in
 * converting from different extensions like xml, yaml, csv in DataConverter
 */
public class Data {
    private String id;
    private String employee_id;
    private String bitcoin_address;
    private String ip_address;
    private String first_name;
    private String last_name;
    private String full_name;
    private String username;
    private String email;
    private String gender;
    private String card_number;
    private String card_balance;
    private String card_currency;
    private String organization;
    private String created_account_data;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getEmployee_id() { return employee_id; }
    public void setEmployee_id(String employee_id) { this.employee_id = employee_id; }

    public String getBitcoin_address() { return bitcoin_address; }
    public void setBitcoin_address(String bitcoin_address) { this.bitcoin_address = bitcoin_address; }

    public String getIp_address() { return ip_address; }
    public void setIp_address(String ip_address) { this.ip_address = ip_address; }

    public String getFirst_name() { return first_name; }
    public void setFirst_name(String first_name) { this.first_name = first_name; }

    public String getLast_name() { return last_name; }
    public void setLast_name(String last_name) { this.last_name = last_name; }

    public String getFull_name() { return full_name; }
    public void setFull_name(String full_name) { this.full_name = full_name; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getCard_number() { return card_number; }
    public void setCard_number(String card_number) { this.card_number = card_number; }

    public String getCard_balance() { return card_balance; }
    public void setCard_balance(String card_balance) { this.card_balance = card_balance; }

    public String getCard_currency() { return card_currency; }
    public void setCard_currency(String card_currency) { this.card_currency = card_currency; }

    public String getOrganization() { return organization; }
    public void setOrganization(String organization) { this.organization = organization; }

    public String getCreated_account_data() { return created_account_data; }
    public void setCreated_account_data(String created_account_data) { this.created_account_data = created_account_data; }

    @Override
    public String toString() {
        return "Data---> " +
                "| id= " + id + ' ' +
                "| employee_id= " + employee_id + ' ' +
                "| bitcoin_address= " + bitcoin_address + ' ' +
                "| ip_address= " + ip_address + ' ' +
                "| first_name= " + first_name + ' ' +
                "| last_name= " + last_name + ' ' +
                "| full_name= " + full_name + ' ' +
                "| username= " + username + ' ' +
                "| email= " + email + ' ' +
                "| gender= " + gender + ' ' +
                "| card_number= " + card_number + ' ' +
                "| card_balance= " + card_balance + ' ' +
                "| card_currency= " + card_currency + ' ' +
                "| organization= " + organization + ' ' +
                "| created_account_data= " + created_account_data + " |";
    }
}
