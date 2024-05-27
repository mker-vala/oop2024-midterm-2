import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class InsuranceCompany {
    private String companyName;
    private List<Customer> customers;

    public InsuranceCompany(String companyName) {
        this.companyName = companyName;
        this.customers = new ArrayList<>();
    }


    public void addCustomer(Customer customer) {
        customers.add(customer);
    }


    public boolean removeCustomerById(String id) {
        for (Customer customer : customers) {
            if (customer.getId().equals(id)) {
                customers.remove(customer);
                return true;
            }
        }
        return false;
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void saveState() {
        try (PrintWriter writer = new PrintWriter(new File("state.csv"))) {
            StringBuilder sb = new StringBuilder();
            for (Customer customer : customers) {
                sb.append(customer.getName());
                sb.append(',');
                sb.append(customer.getId());
                sb.append('\n');
            }
            writer.write(sb.toString());
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred while saving the state: " + e.getMessage());
        }
    }

    public void restoreState() {
        customers.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader("state.csv"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    customers.add(new Customer(parts[0], parts[1]));
                }
            }
        } catch (IOException e) {
            System.out.println("An error occurred while restoring the state: " + e.getMessage());
        }
    }

    @Override
    public String toString() {
        return "InsuranceCompany{" +
                "companyName='" + companyName + '\'' +
                ", customers=" + customers +
                '}';
    }

    public static void main(String[] args) {
        InsuranceCompany company = new InsuranceCompany("SecureLife Insurance");

        Customer customer1 = new Customer("Luka Mkervalishvili", "C001");
        Customer customer2 = new Customer("Tom Cruise", "C002");

        company.addCustomer(customer1);
        company.addCustomer(customer2);
        System.out.println("Before saving state:");
        System.out.println(company);

        // Save state to file
        company.saveState();

        // Clear current customers list
        company.getCustomers().clear();
        System.out.println("After clearing customers:");
        System.out.println(company);

        // Restore state from file
        company.restoreState();
        System.out.println("After restoring state:");
        System.out.println(company);
    }
}
