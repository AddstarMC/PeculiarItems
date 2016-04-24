package au.com.mineauz.peculiaritems.peculiarstats;

public interface SubStatable<T> {
	public PeculiarSubStat of(T type);
	
	public PeculiarSubStat ofEncoded(String subtypeName);
}
