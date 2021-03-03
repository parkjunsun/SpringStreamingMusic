package js.StreamingMusic.domain.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class Record {

    @Id @GeneratedValue
    @Column(name = "Record_ID")
    private Long id;

    private String title;
    private String artist;
    private String img;
    private String songid;

    private int playCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    public void setMember(Member member) {
        this.member = member;
        member.getRecords().add(this);
    }

    public void addCount(int playCount) {
        this.playCount += playCount;
    }
}
