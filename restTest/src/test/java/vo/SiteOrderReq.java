package vo;


import java.io.Serializable;

public class SiteOrderReq implements Serializable{

    private static final long serialVersionUID = -283763423791540651L;
    private String agentCode;
    private String serviceCode;
    private String productCode;
    private int timeAmount;
    private int freeTimeAmount;
    private double price;
    private SiteSpec spec;

    public String getAgentCode() {
        return agentCode;
    }

    public void setAgentCode(String agentCode) {
        this.agentCode = agentCode;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public int getTimeAmount() {
        return timeAmount;
    }

    public void setTimeAmount(int timeAmount) {
        this.timeAmount = timeAmount;
    }

    public int getFreeTimeAmount() {
        return freeTimeAmount;
    }

    public void setFreeTimeAmount(int freeTimeAmount) {
        this.freeTimeAmount = freeTimeAmount;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public SiteSpec getSpec() {
        return spec;
    }

    public void setSpec(SiteSpec spec) {
        this.spec = spec;
    }
}
