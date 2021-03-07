using System;
using System.Linq;
using Microsoft.EntityFrameworkCore;

namespace Retrospector.Tests.Utilities
{
    public static class DatabaseContextExtensions
    {
        public static void AddRange<T>(this DbSet<T> dbSet, int count, Func<int, T> generator) where T : class
        {
            dbSet.AddRange(Enumerable.Range(0, count).Select(generator));
        }

        public static void Add<T>(this DbSet<T> dbSet, int count = 1) where T : class, new()
        {
            dbSet.AddRange(count, i => new T());
        }
    }
}