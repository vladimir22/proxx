package com.vladimir22;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;


public class Task06MergingCalendar {
    /**
     * Your company built an in-house calendar tool called HiCal.
     * You want to add a feature to see the times in a day when everyone is available.
     * <br/>
     * To do this, you’ll need to know when any team is having a meeting.
     * In HiCal, a meeting is stored as an object of a Meeting class with integer variables startTime and endTime.
     * These integers represent the number of 30-minute blocks past 9:00am.
     * <br/>
     * For example:
     * <pre>
     *  new Meeting(2, 3); // meeting from 10:00 – 10:30 am
     *  new Meeting(6, 9); // meeting from 12:00 – 1:30 pm
     * </pre>
     * Write a function mergeRanges() that takes a list of meeting time ranges and returns a list of condensed ranges.
     * <br/>
     * For example, given:
     * <pre>
     *  [ Meeting(0, 1), Meeting(3, 5), Meeting(4, 8), Meeting(10, 12), Meeting(9, 10) ]
     * </pre>
     * your function would return:
     * <pre>
     *  [ Meeting(0, 1), Meeting(3, 8), Meeting(9, 12) ]
     * </pre>
     * <b>Do not assume the meetings are in order. The meeting times are coming from multiple teams.</b>
     * <br/>
     * <b>Write a solution that's efficient even when we can't put a nice upper bound on the numbers representing
     * our time ranges.</b>
     * <br/>
     * </b>Here we've simplified our times down to the number of 30-minute slots past 9:00 am.
     * But we want the function to work even for very large numbers, like Unix timestamps.
     * In any case, the spirit of the challenge is to merge meetings
     * where startTime and endTime don't have an upper bound.
     *
     * @param allMeetings (unsorted) list of meetings
     * @return list of condensed ranges.
     */
    public List<Meeting> mergeRanges (final Collection<Meeting> allMeetings) {

        Comparator<Meeting> comparatorByStartTime = Comparator.comparing(Meeting::getStartTime);
        List<Meeting> sortedMeetings = allMeetings.stream().sorted(comparatorByStartTime).collect(Collectors.toList());

        ArrayList<Meeting> mergedMeetings = new ArrayList<>();

        // Filter out and merge condensed/intersected meetings
        for (Meeting currentMeeting : sortedMeetings ) {

            if (mergedMeetings.size() == 0) {
                mergedMeetings.add(new Meeting(currentMeeting.getStartTime(), currentMeeting.getEndTime()));
                continue;
            }
            Meeting previousMeeting = mergedMeetings.get(mergedMeetings.size()-1);

            if (currentMeeting.getStartTime() > previousMeeting.getEndTime()) {
                mergedMeetings.add(new Meeting(currentMeeting.getStartTime(), currentMeeting.getEndTime()));
                continue; // add meeting that not intersect with previous
            }

            if ( previousMeeting.getEndTime() < currentMeeting.getEndTime()){
                previousMeeting.setEndTime(currentMeeting.getEndTime());
                continue; // shift meeting endTime if it intersects with previous
            }
        }
        return mergedMeetings;
    }

    
    
    @Test
    void mergeRangesTest_IntersectedAndDuplicatedMeetings() {
        Task06MergingCalendar task06MergingCalendar = new Task06MergingCalendar();
        List<Meeting> meetings = List.of(
                new Meeting(0, 1), new Meeting(2, 3), // not intersected meetings
                new Meeting(4, 5), new Meeting(4, 8), new Meeting(4, 7), // intersected meetings
                new Meeting(9, 10), new Meeting(12, 13), new Meeting(9, 10), new Meeting(12, 14)); // duplicated + intersected meetings

        List<Meeting> mergedMeetings = task06MergingCalendar.mergeRanges(meetings);

        System.out.println(mergedMeetings);
        assertThat(mergedMeetings.toArray(), Matchers.arrayContaining(
                new Meeting(0, 1), new Meeting(2, 3), // not intersected meetings stay the same
                new Meeting(4, 8), // intersected meetings have been merged
                new Meeting(9, 10), new Meeting(12, 14))); // repeated meeting has been removed
    }


    @Test
    void mergeRangesTest_MeetingsMergedIntoOne() {
        Task06MergingCalendar task06MergingCalendar = new Task06MergingCalendar();
        List<Meeting> meetings = List.of(
                new Meeting(1, 10), // all meeting will be merged into one
                new Meeting(2, 3), new Meeting(4, 5),
                new Meeting(5, 6), new Meeting(5, 7), new Meeting(5, 8),
                new Meeting(2, 9), new Meeting(2, 11)
                );

        List<Meeting> mergedMeetings = task06MergingCalendar.mergeRanges(meetings);

        System.out.println(mergedMeetings);
        assertThat(mergedMeetings.toArray(), Matchers.arrayContaining(
                new Meeting(1, 11))); // all meetings have been merged into one
    }


    @Test
    void mergeRangesTest_MergeNonSortedMeetings() {
        Task06MergingCalendar task06MergingCalendar = new Task06MergingCalendar();
        List<Meeting> meetings = List.of(
                new Meeting(1, 2), new Meeting(2, 3), new Meeting(1, 3) , new Meeting(2, 4),
                new Meeting(6, 7), new Meeting(5, 6), new Meeting(7, 8),
                new Meeting(9, 10), new Meeting(9, 12), new Meeting(9, 11)
        );
        List<Meeting> mergedMeetings = task06MergingCalendar.mergeRanges(meetings);
        System.out.println(mergedMeetings);
        assertThat(mergedMeetings.toArray(), Matchers.arrayContaining(
                new Meeting(1, 4), // meetings have been sorted and merged
                new Meeting(5, 8),
                new Meeting(9, 12))
        );
    }


    @Data
    @NoArgsConstructor
    public static class Meeting {
        private long startTime;
        private long endTime;

        public Meeting(final long startTime, final long endTime) {
            if (startTime > endTime) {
                throw new IllegalArgumentException("startTime > endTime");
            }
            this.startTime = startTime;
            this.endTime = endTime;
        }

        @Override
        public String toString() {
            return "Meeting{" +
                    "startTime=" + startTime +
                    ", endTime=" + endTime +
                    '}';
        }
    }
}
