using System;
using System.Linq;
using Microsoft.EntityFrameworkCore;

namespace Retrospector.Tests.Utilities
{
    public static class DbSetExtensions
    {
        public static void AddRange<T>(this DbSet<T> dbSet, int count, Func<int, T> generateEntity) where T:class
        {
            dbSet.AddRange(Enumerable.Range(0, count).Select(generateEntity));
        }

        public static T Add<T>(this DbSet<T> dbSet, Action<T> modifyEntity) where T:class, new()
        {
            var entity = new T();
            modifyEntity(entity);
            return dbSet.Add(entity).Entity;
        }

        public static void Add<T>(this DbSet<T> dbSet, int count) where T:class, new()
        {
            dbSet.AddRange(Enumerable.Range(0, count).Select(_ => new T()));
        }

        public static T Add<T>(this DbSet<T> dbSet) where T:class, new()
        {
            return dbSet.Add(new T()).Entity;
        }
    }
}
