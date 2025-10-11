package ay.dev.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "customer")
public class Customer {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name;

	@Column(length = 20)
	private String phone;

	@Column(length = 255)
	private String address;

	@Column(name = "mothers_name", length = 100)
	private String motherName;


	@Column(name = "father_name", length = 100)
	private String fatherName;

	@Column(name = "prefered_name", length = 100)
	private String preferedName;

	@Column(name = "prefered_color", length = 50)
	private String preferedColor;

	@Column(name = "prefered_band", length = 100)
	private String preferedBand;

	// Getters and setters
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
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
	public String getMotherName() {
		return motherName;
	}
	public void setMotherName(String motherName) {
		this.motherName = motherName;
	}
	public String getFatherName() {
		return fatherName;
	}
	public void setFatherName(String fatherName) {
		this.fatherName = fatherName;
	}

	public String getPreferedName() {
		return preferedName;
	}
	public void setPreferedName(String preferedName) {
		this.preferedName = preferedName;
	}
	public String getPreferedColor() {
		return preferedColor;
	}
	public void setPreferedColor(String preferedColor) {
		this.preferedColor = preferedColor;
	}
	public String getPreferedBand() {
		return preferedBand;
	}
	public void setPreferedBand(String preferedBand) {
		this.preferedBand = preferedBand;
	}
}
