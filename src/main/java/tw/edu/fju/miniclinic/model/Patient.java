package tw.edu.fju.miniclinic.model;

import jakarta.persistence.*;

import java.sql.Types;
import java.time.LocalDate;

import org.hibernate.annotations.JdbcTypeCode;

@Entity
@Table(name = "patient")
public class Patient {

    @Id
    @Column(name = "chart_no", length = 20)
    private String chartNo;

    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @Column(name = "gender", length = 10)
    private String gender;

    @JdbcTypeCode(Types.VARCHAR)                       // 告訴 Hibernate：這欄當 VARCHAR 處理
    @Convert(converter = LocalDateConverter.class)     // 使用上面的 Converter 轉換
    @Column(name = "birth_date", columnDefinition = "TEXT")  // 建表時明確指定 TEXT 型別
    private LocalDate birthDate;

    @Column(name = "phone", length = 20)
    private String phone;

    public Patient() {}

    public Patient(String chartNo, String name, String gender,
                   LocalDate birthDate, String phone) {
        this.chartNo = chartNo;
        this.name = name;
        this.gender = gender;
        this.birthDate = birthDate;
        this.phone = phone;
    }

    // Getters & Setters
    public String getChartNo() { return chartNo; }
    public void setChartNo(String chartNo) { this.chartNo = chartNo; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public LocalDate getBirthDate() { return birthDate; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
}
