package com.personalapp.hometeaching.service.impl;

import static com.google.common.collect.Lists.newArrayList;
import static com.personalapp.hometeaching.model.ActionStatus.DUPLICATE;
import static com.personalapp.hometeaching.model.ActionStatus.ERROR;
import static com.personalapp.hometeaching.model.ActionStatus.SUCCESS;
import static java.util.Calendar.LONG;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;
import static java.util.Locale.ENGLISH;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;
import com.personalapp.hometeaching.model.ActionStatus;
import com.personalapp.hometeaching.model.Family;
import com.personalapp.hometeaching.model.Visit;
import com.personalapp.hometeaching.repository.FamilyRepository;
import com.personalapp.hometeaching.repository.VisitRepository;
import com.personalapp.hometeaching.service.VisitService;
import com.personalapp.hometeaching.view.VisitHistoryModel;
import com.personalapp.hometeaching.view.VisitViewModel;

@Service
public class VisitServiceImpl implements VisitService {
	private final Logger logger = getLogger(getClass());

	@Autowired
	private VisitRepository repo;

	@Autowired
	private FamilyRepository familyRepo;

	@Override
	public VisitViewModel save(Visit visit) {
		logger.info("entering the save visit method");
		setVisitProperties(visit);
		ActionStatus status = trySaveVisit(visit);
		return new VisitViewModel(visit, status);
	}

	private ActionStatus trySaveVisit(Visit visit) {
		ActionStatus status = SUCCESS;
		try {
			if (visit.getId() == null) {
				repo.save(visit);
			} else {
				repo.update(visit);
			}
		} catch (Exception e) {
			if (e instanceof MySQLIntegrityConstraintViolationException || e instanceof ConstraintViolationException || e instanceof DataIntegrityViolationException) {
				logger.warn("A visit was already recorded for assignment id: {}, month: {}, year: {}", visit.getAssignmentId(), visit.getMonth(), visit.getYear());
				status = DUPLICATE;
			} else {
				status = ERROR;
			}

		}
		return status;
	}

	private void setVisitProperties(Visit visit) {

		if (visit.getId() == null) {
			setupNewVisit(visit);
		} else {
			updateVisit(visit);
		}
	}

	private void updateVisit(Visit visit) {
		Visit updated = repo.findById(visit.getId());
		visit.setCreated(updated.getCreated());
		visit.setFamilyId(updated.getFamilyId());
		visit.setAssignmentId(updated.getAssignmentId());

		if (visit.getVisited() != null) {
			visit.setVisited(visit.getVisited());
		} else {
			visit.setVisited(false);
		}
		visit.setUpdated(new Date());
	}

	private void setupNewVisit(Visit visit) {
		if (visit.getVisited() != null) {
			visit.setVisited(visit.getVisited());
		} else {
			visit.setVisited(false);
		}
		visit.setCreated(new Date());
	}

	@Override
	public List<VisitViewModel> getByFamilyId(Long familyId) {
		List<VisitViewModel> visits = newArrayList();
		for (Visit visit : repo.getByFamilyId(familyId)) {
			VisitViewModel model = new VisitViewModel(visit);
			visits.add(model);
		}
		return visits;
	}

	@Override
	public List<VisitHistoryModel> getVisitHistory(Integer n) {
		logger.info("Entering the get visit history method, will get last {} visits", n);
		List<VisitHistoryModel> visitHistory = newArrayList();
		for (Family family : familyRepo.getAllFamiliesAndVisits()) {
			List<Visit> nVisits = getLastNVisits(n, family.getVisits());
			visitHistory.add(new VisitHistoryModel(nVisits, family));
		}
		return visitHistory;
	}

	@Override
	public List<String> getLastNMonths(Integer n) {
		List<String> nMonths = newArrayList();
		Calendar calendar = Calendar.getInstance();
		Map<String, Integer> allMonths = calendar.getDisplayNames(MONTH, LONG, ENGLISH);

		for (int i = 0; i < n; i++) {
			Integer month = calendar.get(MONTH) - i;
			if (month < 0) {
				month = 12 + month;
			}
			for (Map.Entry<String, Integer> monthMap : allMonths.entrySet()) {
				if (monthMap.getValue().equals(month)) {
					nMonths.add(0, monthMap.getKey());
					break;
				}
			}
		}

		return nMonths;
	}

	private List<Visit> getLastNVisits(Integer n, Set<Visit> allVisits) {
		List<Visit> nVisits = newArrayList();
		Calendar calendar = Calendar.getInstance();
		Integer year = calendar.get(YEAR);

		for (int i = 0; i < n; i++) {
			Integer month = calendar.get(MONTH) - i;
			if (month < 0) {
				month = 12 + month;
				if (month.equals(11)) {
					year--;
				}
			}

			boolean foundVisit = false;
			for (Visit visit : allVisits) {
				if (visit.getMonth().equals(month + 1) && visit.getYear().equals(year)) {
					nVisits.add(0, visit);
					foundVisit = true;
					break;
				}
			}
			if (!foundVisit) {
				nVisits.add(0, new Visit());
			}
		}

		return nVisits;
	}
}
