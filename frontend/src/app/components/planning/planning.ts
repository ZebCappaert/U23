import { CommonModule } from '@angular/common';
import { Component, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Activity } from '../../models/activity.model';
import { PlanningService } from '../../service/planningService';
import { AuthService } from '../../service/authService';
import { Attendance } from '../../models/attendance.model';

@Component({
  selector: 'app-planning',
  imports: [CommonModule, FormsModule],
  templateUrl: './planning.html',
  styleUrl: './planning.css',
})
// ... imports blijven gelijk

export class Planning implements OnInit {
  activities = signal<Activity[]>([]);
  // We gebruiken een Map om de status (true/false/null) per activiteit bij te houden
  userChoices = new Map<number, boolean | null>();

  newActivity: Activity = { title: '', dateTime: '', location: '' };

  constructor(private planningService: PlanningService, public authService: AuthService) { };

  ngOnInit(): void {
    this.loadActivities();
  }

  loadActivities() {
    this.planningService.getActivities().subscribe(data => {
      this.activities.set(data);
      this.initializeUserChoices(data);
    });
  }

  // STAP 1: Bestaande keuzes uit de data halen
  private initializeUserChoices(activities: Activity[]) {
    const currentUserId = this.authService.getUser()?.id;

    activities.forEach(activity => {
      if (activity.id) {
        // Zoek of de huidige gebruiker al in de attendances van deze activiteit staat
        const myAttendance = activity.attendances?.find(a => a.user?.id === currentUserId);
        this.userChoices.set(activity.id, myAttendance ? myAttendance.isPresent : null);
      }
    });
  }

  saveActivity() {
    if (this.newActivity.title && this.newActivity.dateTime) {
      this.planningService.createActivity(this.newActivity).subscribe(() => {
        this.loadActivities();
        this.newActivity = { title: '', dateTime: '', location: '' };
      });
    }
  }

  submitAttendance(activityId: number | undefined, present: boolean) {
    const currentUser = this.authService.getUser();
    if (!currentUser || !activityId) return;

    const attendance: Attendance = {
      user: { id: currentUser.id! },
      activity: { id: activityId },
      isPresent: present
    };

    this.planningService.saveAttendance(attendance).subscribe({
      next: (res) => {
        // STAP 2: De lokale Map direct updaten voor visuele feedback
        this.userChoices.set(activityId, present);
        console.log("Status updated locally");
      },
      error: (err) => console.log("Error during saving: ", err)
    });
  }

  // STAP 3: Helper methode voor CSS classes in de HTML
  checkStatus(activityId: number | undefined): boolean | null {
    return activityId ? this.userChoices.get(activityId) ?? null : null;
  }

  getAttendanceCount(act: Activity): number {
    if (!act.attendances) return 0;
    return act.attendances.filter(a => a.isPresent).length;
  }

  deleteActivity(id: number | undefined) {
    if (!id) return;

    if (confirm('Weet je zeker dat je deze activiteit wilt verwijderen?')) {
      this.planningService.deleteActivity(id).subscribe({
        next: () => {
          this.loadActivities(); // Ververs de lijst direct
        },
        error: (err) => console.error("Fout bij verwijderen:", err)
      });
    }
  }
}