import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Activity } from '../models/activity.model';
import { Attendance } from '../models/attendance.model';

@Injectable({
    providedIn: 'root'
})
export class PlanningService {
    private activityUrl = 'http://localhost:8080/api/activities';
    private attendanceUrl = 'http://localhost:8080/api/attendance';

    constructor(private http: HttpClient) { }

    getActivities(): Observable<Activity[]> {
        return this.http.get<Activity[]>(this.activityUrl);
    }

    createActivity(activity: Activity): Observable<Activity> {
        return this.http.post<Activity>(this.activityUrl, activity);
    }

    getAttendanceForActivity(activityId: number): Observable<Attendance[]> {
        return this.http.get<Attendance[]>(`${this.attendanceUrl}/${activityId}`);
    }

    saveAttendance(attendance: Attendance): Observable<Attendance> {
        return this.http.post<Attendance>(this.attendanceUrl, attendance);
    }

    deleteActivity(id: number): Observable<void> {
        return this.http.delete<void>(`${this.activityUrl}/${id}`);
    }
}