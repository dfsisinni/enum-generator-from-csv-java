public enum NAME { 

CANADA("2014-05-07 14:23","canada",1),
USA(null,null,2);

private final LocalDateTime blob;
private final String x;
private final int y;


NAME(LocalDateTime blob,String x,int y) {
this.blob = blob;
this.x = x;
this.y = y
}

}