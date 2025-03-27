public class Project {
    private boolean isVisible = true; //on or off
    private String OpentoUserGroup = ""; //single or married

    private String ProjectName;
    private String Neighborhood;
    private String Type1;
    private int NoOfUnitsForType1;
    private int SellingPriceForType1;
    private String Type2;
    private int NoOfUnitsForType2;
    private int SellingPriceForType2;
    private String ApplicantOpeningDate;
    private String ApplicantClosingDate;
    private String ManagerInCharge;
    private int OfficerSlots;
    private String[] OfficersInCharge;

    Project(String PN, String n, String T1, int T1no, int SPT1, String T2, int T2no, int SPT2, String AOD, String ACD, String MIC, int OS, String OIC) {
        this.ProjectName = PN;
        this.Neighborhood = n;
        this.Type1 = T1;
        this.NoOfUnitsForType1 = T1no;
        this.SellingPriceForType1 = SPT1;
        this.Type2 = T2;
        this.NoOfUnitsForType2 = T1no;
        this.SellingPriceForType2 = SPT2;
        this.ApplicantOpeningDate = AOD;
        this.ApplicantClosingDate = ACD;
        this.ManagerInCharge = MIC;
        this.OfficerSlots = OS;
        this.OfficersInCharge = new String[OS];
        for (int i = 0; i < OfficerSlots; i++) {
            OfficersInCharge[i] = "";
        }
        int index = 0;
        for (String Officer : OIC.split(","))  {
            this.OfficersInCharge[index] = Officer;
            index++;
        }  
    }

    public boolean isVisible() {
        return isVisible;
    }
    public void setVisibility(boolean val) {
        this.isVisible = val;
    }
    public String getGroupProjOpento() {
        return OpentoUserGroup;
    }
    public String setGroupProjOpento(String group) {
        this.OpentoUserGroup = group;
    }


    public String getProjectName() {
        return ProjectName;
    }
    public void setProjectName(String PN) {
        this.ProjectName = PN;
    }

    public String getNeighbourhood() {
        return Neighborhood;
    }
    public void setNeighbourhood(String n) {
        this.Neighborhood = n;
    }

    public String getType1() {
        return Type1;
    }
    public void setType1(String T1) {
        this.Type1 = T1;
    }

    public int getNoOfUnitsForType1() {
        return NoOfUnitsForType1;
    }
    public void setNoOfUnitsForType1(int T1no) {
        this.NoOfUnitsForType1 = T1no;
    }

    public int getSellingPriceForType1() {
        return SellingPriceForType1;
    }
    public void setSellingPriceForType1(int SPT1) {
        this.SellingPriceForType1 = SPT1;
    }

    public String getType2() {
        return Type2;
    }
    public void setType2(String T2) {
        this.Type2 = T2;
    }

    public int getNoOfUnitsForType2() {
        return NoOfUnitsForType2;
    }
    public void setNoOfUnitsForType2(int T2no) {
        this.NoOfUnitsForType2 = T2no;
    }

    public int getSellingPriceForType2() {
        return SellingPriceForType2;
    }
    public void setSellingPriceForType2(int SPT2) {
        this.SellingPriceForType2 = SPT2;
    }

    public String getApplicantOpeningDate() {
        return ApplicantOpeningDate;
    }
    public void setApplicantOpeningDate(String AOD) {
        this.ApplicantOpeningDate = AOD;
    }

    public String getApplicantClosingDate() {
        return ApplicantClosingDate;
    }
    public void setApplicantClosingDate(String ACD) {
        this.ApplicantClosingDate = ACD;
    }

    public String getManagerInCharge() {
        return ManagerInCharge;
    }
    public void setManagerInCharge(String MIC) {
        this.ManagerInCharge = MIC;
    }

    public int getNoOfOfficersCurrentlyAssigned() {
        int size = 0;
        for (String officer : OfficersInCharge) {
            if (!officer.equals("")) {
                size++;
            }
        }
        return size;
    }

    public int getOfficerSlots() {
        return OfficerSlots;
    }
    public void setOfficerSlots(int OS) {
        if (OS < getNoOfOfficersCurrentlyAssigned()) {
            System.out.println("There are more officers assigned than the slots you are trying to assign! Consider removing existing officers to reduce number of slots.");
            return;
        }
        this.OfficerSlots = OS;
        String[] Old_OfficersInCharge = OfficersInCharge;
        String[] New_OfficersInCharge = new String[OS];
        for (int i = 0; i < OfficerSlots; i++) {
            New_OfficersInCharge[i] = "";
        }
        int index = 0;
        for (String officer : Old_OfficersInCharge)  {
            if (!officer.equals("")) {
                New_OfficersInCharge[index] = officer;
                index++;
            }
        }  
        this.OfficersInCharge = New_OfficersInCharge;
    }

    public String[] getOfficersInCharge() {
        return OfficersInCharge;
    }
    public void addOfficersInCharge(String Officer) {
        if (getNoOfOfficersCurrentlyAssigned() == OfficerSlots) {
            System.out.println("There are no more slots to assign anymore new officers!");
            return;
        }
        int index = 0;
        for (String off : OfficersInCharge) {
            if (off.equals("")) {
                OfficersInCharge[index] = Officer;
                break;
            }
            index++;
        }
    }
    public void deleteOfficerInChage(String Officer) {
        int index = 0;
        for (String off : OfficersInCharge) {
            if (off.equals(Officer)) {
                OfficersInCharge[index] = "";
                return;
            }           
            index++;
        }
        System.out.println("Cannot find such an officer in project!");
    }

}
