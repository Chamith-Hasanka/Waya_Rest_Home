public class User {
    private String username;
    private String phone;
    private String dob;
    private String email;
    private String password;

    public User(String username, String phone, String dob, String email, String password) {
        this.username = username;
        this.phone = phone;
        this.dob = dob;
        this.email = email;
        this.password = password;
    }

    // Getters
    public String getUsername() { 
        return username; 
    }
    public String getPhone() { 
        return phone; 
    }
    public String getDob() { 
        return dob; 
    }
    public String getEmail() { 
        return email; 
    }
    public String getPassword() { 
        return password; 
    }

    // Setters
    public void setUsername(String username) { 
        this.username = username; 
    }
    public void setPhone(String phone) { 
        this.phone = phone; 
    }
    public void setDob(String dob) { 
        this.dob = dob; 
    }
    public void setEmail(String email) { 
        this.email = email; 
    }
    public void setPassword(String password) { 
        this.password = password; 
    }
}
