package InteractableAttributePackage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import User.*;
import User.Applicant.MaritalStatus;

// ProjectDetails is a class that contains all the details of the project
public class ProjectDetails {

    public enum Location {
        BUGIS, LITTLE_INDIA, CHINATOWN, MARINA_BAY, CLARKE_QUAY, ORCHARD, DEMPSEY, QUEENSTOWN, FARRER_PARK, REDHILL, 
        JALAN_BESAR, SENTOSA_TANJONG, PAGAR_KALLANG, KEONG_SAIK, TIONG_BAHRU, BEDOK, PASIR_RIS, CHANGI, PAYA_LEBAR, 
        EAST_COAST, SIGLAP, GEYLANG, TAMPINES, KATONG, TANAH_MERAH, HOUGANG, SENGKANG, PUNGGOL, SERANGOON, TAI_SENG, 
        SELETAR, ANG_MO_KIO, THOMSON, BALIESTER, SEMBAWANG, KRANJI, YISHUN, WOODLANDS, LIM_CHU_KANG, TOA_PAYOH, BUKIT_PANJANG, 
        CLEMENTI, BUKIT_TIMAH, HILLVIEW, BUKIT_BATOK, HOLLAND_VILLAGE, BUONA_VISTA,JURONG ,CHOA_CHU_KANG , JURONG_WEST, JURONG_EAST
    }

    public boolean activeStatus = true;
    public MaritalStatus OpentoUserGroup;
    //regarding editing project name: since it's a unique identifier, should it be allowed to be changed? might mess up how enquires/requests are identified
    public String ProjectName;
    public Location Neighborhood;
    //all projects are assumed to have 2 room and 3 room flats, so no need to specify type of flats in constructor
    public int SellingPrice_2Room; 
    public int SellingPrice_3Room;
    public int NoOfUnitsLeft_2Room;
    public int NoOfUnitsLeft_3Room;
    public LocalDate OpenDate;
    public LocalDate CloseDate;
    public HDB_Manager Manager;
    //hardcoded max is 10, but managers can decide what is the max slots as long as it is less than 10
    public int OfficerSlots;
    public ArrayList<HDB_Officer> OfficerList;

    public ProjectDetails(String PN, String n, int SP2R, int SP3R, int Units2R, int Units3R, String AOD, String ACD, HDB_Manager M
    , int OS, ArrayList<HDB_Officer> OIC, boolean v, String group) {
        this.ProjectName = PN;
        this.Neighborhood = Location.valueOf(n.toUpperCase());
        this.SellingPrice_2Room = SP2R;
        this.SellingPrice_3Room = SP3R;
        this.NoOfUnitsLeft_2Room = Units2R;
        this.NoOfUnitsLeft_3Room = Units3R;
        this.OpenDate = LocalDate.parse(AOD, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        this.CloseDate = LocalDate.parse(ACD, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        this.Manager = M;
        //NOTE: managers can only handle ONE 'active' project at at time (can only create new ones if not handling an active one currently)
        //definiton of active: visibility turned ON + within open date and close date
        //however, they can still handle approving applications and rejecting withdrawals, and reply to enquiries of their projects
        //if there are any leftover requests/enquiries that haven't been handled yet.
        this.OfficerSlots = OS;
        this.OfficerList = OIC;
        this.activeStatus = v;
        //maybe include a check to automatically set visibility if past closing date?
        this.OpentoUserGroup = MaritalStatus.valueOf(group.toUpperCase());

    }

}
