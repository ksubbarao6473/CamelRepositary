

import java.io.IOException;

public class RestClient {
	
	public static void main(String args[]) throws IOException {
		
		int i=1;
		Client c = new Client().create();
//		Set<Integer> s = new TreeSet<Integer>();
//		BufferedWriter writer = new BufferedWriter(new FileWriter(new File("C:/rlog/reports/s.dat")));
//		while(i != 0 ) {
//			try {
//				String endPoint = "https://stage-eimmdmlocation.wal-mart.com/locationservices/searchbu/businessunits?q=buType.code:(6+7+11+88+150)&q=countryCode:US&q=pageNum:"+i+"&q=numItemsPerPage:500";
//				//"&q=market.code:[1+TO+998]&q=region.code:[1+TO+98]&q=status.code:[0+TO+6]&q=buType.code:(6+7+11+88+150)&q=distinct:region.code&q=pageNum:1&q=numItemsPerPage:100&q=baseDivision.code:1";
//				
//				System.out.println("Current Endpoint: "+endPoint);
//				WebResource resource = c.resource(endPoint);
//				ClientResponse response = resource.type(MediaType.APPLICATION_FORM_URLENCODED).get(ClientResponse.class);
//				BusinessUnitsDetail bdetails = response.getEntity(BusinessUnitsDetail.class);
//				List<BusinessUnitsDetail.BusinessUnitDetail> bu = bdetails.getBusinessUnitDetail();
//				//System.out.println(bu.size());
//				BusinessUnitsDetail.BusinessUnitDetail.BusinessUnit bunt=null;
//				for(final BusinessUnitsDetail.BusinessUnitDetail bunit:bu ) {
//					bunt = bunit.getBusinessUnit();
//					//System.out.println(bunt.getName());
//					System.out.println(bunt.getNumber());
//					s.add(bunt.getNumber());
//					writer.write(String.valueOf(bunt.getNumber()));
//					writer.newLine();
//					writer.flush();
//				}
//				//System.out.println("Current I Value: "+i);
//				i++;
//				//System.out.println("Current List Size: "+s.size());
//			}
//			catch(Exception e) {
//				e.printStackTrace();
//				i=0;
//			}
//		}
		
		String endPoint = "http://vlinkd3.wal-mart.com:62319/FetchOnhandInfo?StoreNumber=1505&NDCNumber=54458096410";
		System.out.println("Current Endpoint: "+endPoint);
		WebResource resource = c.resource(endPoint);
		ClientResponse response = resource.get(ClientResponse.class);
		String s = response.getEntity(String.class);
		System.out.println(s);
		System.out.println(response);
		

	}
}
