# Chaotic-Subway
Subway application suggesting paths considering the congestion of cabins 

## View:
- [x] Overall Layouts
- [x] Search Bar(Navigation)
- [ ] Main View
    - [x] Diagonal scroll
    - [x] SVG Library
    - [x] Station map
    - [ ] Popup(on station click)
    - [x] Search station name
    - [x] Set time
- [x] Result View
    - [x] Station Adapter
- [x] Congestion View

## Data
- [x] Crawl station data
- [x] Crawl timetable data
- [x] Crawl yearly congestion data
- [x] Parse all data
- [x] Congestion Prediction


## Route Searching
- [x] Station, Line, Train class
- [x] Ford Algorithm
- [ ] Algorithm concerning Time Table
- [x] Draw specific search results
- [ ] Number of transfers, Estimated Time, Fee
- [x] Apply to result view


## Structure
    /app/src/main
    ├── /assets
	│   └── TrainNo 					// Train Info CSV
	│   └── stations					// Station Info CSV
    │   └── ...   	                                	// More CSV Datas
    ├── /java/com/example/chaoticsubway
    │   ├── MainActivity.java                               // Activity for main view
	│   └──	ResultActivity.java                             // Activity for result view			
	│   └──	CongestionActivity.java                         // Activity for congestion view		
	│   └──	RouteSearcher.java                              // Route Search			
	│   └──	/main                                           // Manage all parsed datas
    │   └── /parse                                          // Data crawl from API
    │   └── /getInfo                                        // TimeTable parsing 
    │   └── /database                                       // DB Trial
    │   └──	...			                        // Adapters and classes for route search
    ├── /res/layout
    │   └──	activity_main.xml			        // Main View
    │   └──	activity_result.xml			        // Search result View
    │   └──	activity_congestion.xml			        // Congestion View

## Screenshots
<table>
    <tr>
        <td><img src="https://user-images.githubusercontent.com/57870500/85302262-e6bddd80-b4e3-11ea-9947-f7c02673b9f5.png" /></td>
        <td><img src="https://user-images.githubusercontent.com/57870500/85302274-e9b8ce00-b4e3-11ea-83dd-91d90752019a.png"/></td>
        <td><img src="https://user-images.githubusercontent.com/57870500/85302266-e7ef0a80-b4e3-11ea-9f4c-d2d148531818.png"/></td>
    </tr>
</table>